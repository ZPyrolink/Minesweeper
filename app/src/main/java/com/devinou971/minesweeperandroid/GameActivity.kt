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
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.scale
import com.devinou971.minesweeperandroid.classes.MinesweeperBoard
import com.devinou971.minesweeperandroid.extensions.*
import com.devinou971.minesweeperandroid.services.TimerService
import com.devinou971.minesweeperandroid.storageclasses.AppDatabase
import com.devinou971.minesweeperandroid.storageclasses.GameData
import com.devinou971.minesweeperandroid.utils.*
import java.util.*
import java.util.concurrent.TimeUnit

class GameActivity : AppCompatActivity() {
    private lateinit var bombIcon: Bitmap
    private lateinit var flagIcon: Bitmap
    private lateinit var tileIcon: Bitmap

    private lateinit var gameMode: Difficulty

    private lateinit var gameBoard: MinesweeperBoard
    private lateinit var gameView: SurfaceView
    private lateinit var labelNbFlagsRemaining: TextView

    //#region Bind

    private lateinit var gameOverPopup: ConstraintLayout
    private lateinit var gameOverText: TextView
    private lateinit var anotherChance: Button
    private lateinit var replay: Button

    //#endregion

    private var nbBombs = 0
    private var nbRows = 0
    private var nbCols = 0
    private var cellSize: Int = 0

    private var quit = false

    // --------- DO WE WANT TO FLAG A SLOT OR REVEAL A SLOT ? ---------
    enum class Mode(val icon: Int) {
        REVEAL(R.drawable.pickaxeicon),
        FLAG(R.drawable.flagicon);

        val next
            get() = when (this) {
                REVEAL -> FLAG
                FLAG -> REVEAL
            }
    }

    private var mode: Mode = Mode.REVEAL

    // Timer Info
    private lateinit var serviceIntent: Intent
    private var time: Double = 0.0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        getExtras(intent)
        getIcons()
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
        setOnClickListeners(R.id.reloadBoard, R.id.replayButton) { replay() }
        setOnClickListeners(R.id.returnMenuButton) { goToMenu() }

        // --------- ONCLICK EVENT TO SWITCH BETWEEN FLAG AND REVEAL MODE ---------
        findViewById<ImageView>(R.id.switchmode).apply {
            setBackgroundResource(Settings.theme[mode.icon])
            setOnClickListener {
                mode = mode.next
                setBackgroundResource(Settings.theme[mode.icon])
            }
        }

        labelNbFlagsRemaining.text = gameBoard.nbFlags.toString()
        setOnClickListeners(R.id.anotherChance) { revive() }

        // --------- ONCE THE VIEW IS AVAILABLE, WE DRAW THE GRID ON IT ---------
        gameView.viewTreeObserver.addOnWindowFocusChangeListener { if (!quit) drawGrid() }

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(ExtraUtils.TIMER_UPDATED.name))
    }

    /**
     * FETCHING ALL THE VIEWS
     */
    private fun bindViews() {
        gameView = findViewById(R.id.gameView)
        gameOverPopup = findViewById(R.id.gameOverPopup)
        gameOverText = findViewById(R.id.gameOverText)
        anotherChance = findViewById(R.id.anotherChance)
        replay = findViewById(R.id.replayButton)
        labelNbFlagsRemaining = findViewById(R.id.nbFlagsRemaining)
    }

    private fun setGameOverView(win: Boolean) {
        gameOverPopup.setVisible()

        if (win) {
            gameOverText.text = getString(R.string.you_won_string)
            anotherChance.setGone()
            replay.layoutParams.height = 75.applyDim(CpxUnit.DIP)
        } else {
            gameOverText.text = getString(R.string.gameover)
            anotherChance.setVisible()
            replay.layoutParams.height = 61.applyDim(CpxUnit.DIP)
        }
    }

    /**
     * GETTING THE DIFFERENT VALUES SENT FROM OTHER ACTIVITIES
     */
    private fun getExtras(intent: Intent) = intent.apply {
        nbBombs = getExtra(ExtraUtils.NB_BOMBS, 10)
        nbRows = getExtra(ExtraUtils.NB_ROWS, 10)
        nbCols = getExtra(ExtraUtils.NB_COLS, 10)
        cellSize = getExtra(ExtraUtils.CELL_SIZE, 100)
        gameMode = getExtra(ExtraUtils.DIFFICULTY, Difficulty.CUSTOM)
    }

    private fun setOnClickListeners(@IdRes vararg viewIds: Int, listener: View.OnClickListener) {
        for (id in viewIds)
            findViewById<View>(id).setOnClickListener(listener)
    }

    private fun getIcons() {
        bombIcon = getIcon(Settings.theme[R.drawable.bombicon])
        flagIcon = getIcon(Settings.theme[R.drawable.flagicon])
        tileIcon = getIcon(Settings.theme[R.drawable.emptytile])
    }

    private fun getIcon(@DrawableRes resource: Int) =
        BitmapFactory.decodeResource(resources, resource).scale(cellSize, cellSize, false)

    private fun gridClickedEvent(position: PointF) {
        act(position, mode)

        when (mode) {
            Mode.REVEAL -> {
                val toastText: String
                val win: Boolean

                when {
                    gameBoard.won() -> {
                        toastText = "You win GG!"
                        win = true

                        Thread {
                            AppDatabase.getAppDataBase(this).gameDataDAO()
                                .insertGameData(GameData(null, this.time.toInt(), this.gameMode))
                        }.start()
                    }
                    gameBoard.gameOver -> {
                        toastText = "Game Over"
                        win = false
                    }
                    else -> return
                }

                stopTimer()

                Toast.makeText(this, toastText, Toast.LENGTH_LONG).show()
                gameView.setGone()
                setGameOverView(win)
            }

            Mode.FLAG -> labelNbFlagsRemaining.text = gameBoard.nbFlags.toString()
        }
    }

    private fun replay() {
        resetTimer()
        gameBoard = MinesweeperBoard(nbRows, nbCols, nbBombs)
        setGameView()
    }

    private fun revive() {
        gameBoard.revive()
        setGameView()
        startTimer()
    }

    private fun setGameView() {
        gameOverPopup.setGone()
        gameView.setVisible()

        labelNbFlagsRemaining.text = gameBoard.nbFlags.toString()
        drawGrid()
    }

    private fun goToMenu() {
        quit = true
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }

    private fun gridPosition(position: PointF) =
        Point((position.x / cellSize).toInt(), (position.y / cellSize).toInt())

    private fun act(position: PointF, mode: Mode) {
        if (mode == Mode.REVEAL && gameBoard.isFirstTouch)
            startTimer()

        val gridPosition = gridPosition(position)

        if (gridPosition.x !in 0 until gameBoard.nbCols ||
            gridPosition.y !in 0 until gameBoard.nbRows
        )
            return

        gameBoard.apply {
            when (mode) {
                Mode.REVEAL -> this::reveal
                Mode.FLAG -> this::flag
            }.invoke(gridPosition)
        }

        drawGrid()
    }

    private fun drawGrid() {
        val canvas = gameView.holder.lockCanvas()
        canvas.drawColor(applicationContext.getColor(R.color.bgColor), PorterDuff.Mode.CLEAR)
        val textOffset = Point(cellSize / 3, cellSize * 2 / 3)

        fun drawBitmap(icon: Bitmap, x: Int, y: Int) =
            canvas.drawBitmap(
                icon, x * cellSize.toFloat(), y * cellSize.toFloat(),
                Paint(0)
            )

        val icons = mapOf(
            "B" to bombIcon,
            "F" to flagIcon,
            "#" to tileIcon
        )

        for (p in Point().until(gameBoard.rows, gameBoard.columns))
            when (val res = gameBoard[p].toString()) {
                "B", "F", "#" -> drawBitmap(icons[res]!!, p.x, p.y)
                "0" -> {} // We don't display when there are no bombs
                else -> {
                    canvas.drawText(
                        res,
                        p.x * cellSize + textOffset.x.toFloat(),
                        p.y * cellSize + textOffset.y.toFloat(),
                        Paint(Settings.colors[res.toInt() - 1], 50f)
                    )
                }
            }
        gameView.holder.unlockCanvasAndPost(canvas)
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getExtra(ExtraUtils.TIME_EXTRA, .0)
            val seconds = time.toInt() % TimeUnit.MINUTES.toSeconds(1)
            val minutes = TimeUnit.SECONDS.toMinutes(time.toLong()).toInt()
            findViewById<TextView>(R.id.gameTimerView).text = resources.getString(
                R.string.game_timer,
                minutes.toString().padStart(2, '0'),
                seconds.toString().padStart(2, '0')
            )
        }
    }

    private fun startTimer() {
        serviceIntent.putExtra(ExtraUtils.TIME_EXTRA, time)
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
