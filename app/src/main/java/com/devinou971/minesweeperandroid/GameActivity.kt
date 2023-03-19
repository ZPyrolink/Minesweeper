package com.devinou971.minesweeperandroid

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.*
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.scale
import com.devinou971.minesweeperandroid.classes.MinesweeperBoard
import com.devinou971.minesweeperandroid.extensions.Paint
import com.devinou971.minesweeperandroid.extensions.PointF
import com.devinou971.minesweeperandroid.extensions.setGone
import com.devinou971.minesweeperandroid.extensions.setVisible
import com.devinou971.minesweeperandroid.services.TimerService
import com.devinou971.minesweeperandroid.storageclasses.AppDatabase
import com.devinou971.minesweeperandroid.storageclasses.GameData
import com.devinou971.minesweeperandroid.utils.*

class GameActivity : AppCompatActivity() {
    private lateinit var bombIcon: Bitmap
    private lateinit var flagIcon: Bitmap
    private lateinit var tileIcon: Bitmap

    private lateinit var gameMode: Difficulty

    private val paints = arrayOf(
        Paint(Color.BLUE, 50f),
        Paint(Color.GREEN, 50f),
        Paint(Color.RED, 50f),
        Paint(Color.rgb(0, 0, 127), 50f),
        Paint(Color.rgb(127, 0, 0), 50f),
        Paint(Color.rgb(255, 192, 203), 50f),
        Paint(Color.MAGENTA, 50f),
        Paint(Color.CYAN, 50f),
        Paint(Color.YELLOW, 50f)
    )

    private lateinit var gameBoard: MinesweeperBoard
    private lateinit var gameView: SurfaceView
    private lateinit var labelNbFlagsRemaining: TextView

    private lateinit var playerWin: ConstraintLayout
    private lateinit var playerLose: ConstraintLayout

    private var nbBombs = 0
    private var nbRows = 0
    private var nbCols = 0
    private var cellSize: Int = 0

    private var quit = false

    // --------- DO WE WANT TO FLAG A SLOT OR REVEAL A SLOT ? ---------
    enum class Mode(val icon: Int) {
        REVEAL(R.drawable.pickaxeicon),
        FLAG(R.drawable.flagicon);

        fun next() = when (this) {
            REVEAL -> FLAG
            FLAG -> REVEAL
        }
    }

    private var mode: Mode = Mode.REVEAL

    // Timer Info
    private lateinit var serviceIntent: Intent
    private var time: Double = 0.0

    /**
     * FETCHING ALL THE VIEWS
     */
    private fun bindViews() {
        gameView = findViewById(R.id.gameView)
        playerWin = findViewById(R.id.playerWin)
        playerLose = findViewById(R.id.playerLose)
        labelNbFlagsRemaining = findViewById(R.id.nbFlagsRemaining)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        Thread {
            AppDatabase.getAppDataBase(this).gameDataDAO().getAllGameData().apply {
                while (moveToNext()) {
                    val index = getColumnIndexOrThrow("game_type")
                    val lastName = getString(index)
                    println(lastName)
                    println(columnNames)
                }
            }
        }.start()

        // --------- GETTING THE DIFFERENT VALUES SENT FROM OTHER ACTIVITIES ---------
        intent.apply {
            nbBombs = getIntExtra(ExtraUtils.NB_BOMBS, 10)
            nbRows = getIntExtra(ExtraUtils.NB_ROWS, 10)
            nbCols = getIntExtra(ExtraUtils.NB_COLS, 10)
            cellSize = getIntExtra(ExtraUtils.CELL_SIZE, 100)
            gameMode = getExtra(ExtraUtils.DIFFICULTY, Difficulty.CUSTOM)
        }

        fun getIcon(resource: Int) =
            BitmapFactory.decodeResource(resources, resource).scale(cellSize, cellSize, false)

        bombIcon = getIcon(R.drawable.bombicon)
        flagIcon = getIcon(R.drawable.flagicon)
        tileIcon = getIcon(R.drawable.emptytile)

        bindViews()

        gameBoard = MinesweeperBoard(nbRows, nbCols, nbBombs)

        // --------- CREATE ON CLICK EVENT FOR CANVAS ---------
        gameView.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP)
                gridClickedEvent(PointF(motionEvent))

            return@setOnTouchListener true
        }

        gameView.setZOrderOnTop(true)
        gameView.holder.setFormat(PixelFormat.TRANSPARENT)

        // --------- ONCLICK EVENT FOR THE "REPLAY" BUTTON
        for (id in arrayOf(R.id.reloadBoard, R.id.replayButton, R.id.replayButton2))
            findViewById<View>(id).setOnClickListener { replay() }

        for (id in arrayOf(R.id.returnMenuButton, R.id.returnMenuButton2))
            findViewById<Button>(id).setOnClickListener { goToMenu() }

        // --------- ONCLICK EVENT TO SWITCH BETWEEN FLAG AND REVEAL MODE ---------
        findViewById<ImageView>(R.id.switchmode).apply {
            setBackgroundResource(R.drawable.pickaxeicon)
            setOnClickListener {
                mode = mode.next()
                setBackgroundResource(mode.icon)
            }
        }

        labelNbFlagsRemaining.text = gameBoard.nbFlags.toString()
        findViewById<Button>(R.id.anotherChance).setOnClickListener { revive() }

        // --------- ONCE THE VIEW IS AVAILABLE, WE DRAW THE GRID ON IT ---------
        gameView.viewTreeObserver.addOnWindowFocusChangeListener { if (!quit) drawGrid() }

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
    }

    private fun gridClickedEvent(position: PointF) {
        act(position, mode)

        when (mode) {
            Mode.REVEAL -> {
                if (gameBoard.isFirstTouch)
                    startTimer()

                val toastText: String
                val nextScreen: ConstraintLayout?

                when {
                    gameBoard.won() -> {
                        toastText = "You win GG!"
                        nextScreen = playerWin

                        Thread {
                            AppDatabase.getAppDataBase(this).gameDataDAO()
                                .insertGameData(GameData(null, this.time.toInt(), this.gameMode))
                        }.start()
                    }
                    gameBoard.gameOver -> {
                        toastText = "Game Over"
                        nextScreen = playerLose
                    }
                    else -> return
                }

                stopTimer()

                Toast.makeText(this, toastText, Toast.LENGTH_LONG).show()
                gameView.setGone()
                nextScreen.setVisible()
            }

            Mode.FLAG -> labelNbFlagsRemaining.text = gameBoard.nbFlags.toString()
        }
    }

    private fun setGameView() {
        playerWin.setGone()
        playerLose.setGone()
        gameView.setVisible()

        labelNbFlagsRemaining.text = gameBoard.nbFlags.toString()
        drawGrid()
    }

    private fun replay() {
        resetTimer()
        gameBoard = MinesweeperBoard(nbRows, nbCols, nbBombs)

        setGameView()
    }

    private fun revive() {
        gameBoard.gameOver = false
        gameBoard.revive()

        setGameView()

        startTimer()
    }

    private fun goToMenu() {
        quit = true
        startActivity(Intent(this, MenuActivity::class.java))
    }

    private fun gridPosition(position: PointF) =
        Point((position.x / cellSize).toInt(), (position.y / cellSize).toInt())

    private fun act(position: PointF, mode: Mode) {
        val gridPosition = gridPosition(position)

        if (gridPosition.x !in 0 until gameBoard.nbCols ||
            gridPosition.y !in 0 until gameBoard.nbRows
        )
            return

        gameBoard.apply {
            when (mode) {
                Mode.REVEAL -> this::reveal
                Mode.FLAG -> this::flag
            }(gridPosition)
        }
    }

    private fun drawGrid() {
        val canvas = gameView.holder.lockCanvas()
        canvas.drawColor(this.applicationContext.getColor(R.color.bgColor), PorterDuff.Mode.CLEAR)
        val textOffset = Point(cellSize / 3, cellSize * 2 / 3)

        fun drawBitmap(icon: Bitmap, x: Int, y: Int) =
            canvas.drawBitmap(icon, x * cellSize.toFloat(), y * cellSize.toFloat(), Paint(0))

        for (y in 0 until gameBoard.grid.size) {
            for (x in 0 until gameBoard.grid[0].size) {
                when (val res = gameBoard.grid[y][x].toString()) {
                    "B", "F", "#" -> drawBitmap(
                        when (res) {
                            "B" -> bombIcon
                            "F" -> flagIcon
                            "#" -> tileIcon
                            else -> throw IllegalArgumentException()
                        }, x, y
                    )
                    "0" -> {} // We don't display when there are no bombs
                    else -> {
                        canvas.drawText(
                            res,
                            x * cellSize + textOffset.x.toFloat(),
                            y * cellSize + textOffset.y.toFloat(),
                            paints[res.toInt() - 1]
                        )
                    }
                }
            }
        }
        gameView.holder.unlockCanvasAndPost(canvas)
    }


    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            val seconds = time.toInt() % 60
            val minutes = (time / 60).toInt()
            findViewById<TextView>(R.id.gameTimerView).text = resources.getString(
                R.string.game_timer,
                minutes.toString().padStart(2, '0'),
                seconds.toString().padStart(2, '0')
            )
        }
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
    }

    private fun resetTimer() {
        stopTimer()
        time = 0.0
        findViewById<TextView>(R.id.gameTimerView).text =
            resources.getString(R.string.game_timer, "00", "00")
    }

    private fun stopTimer() {
        stopService(serviceIntent)
    }

    override fun onDestroy() {
        stopTimer()
        super.onDestroy()
    }
}
