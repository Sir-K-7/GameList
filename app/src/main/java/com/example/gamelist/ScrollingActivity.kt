package com.example.gamelist

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*
import java.io.File
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class ScrollingActivity : AppCompatActivity() {
    //Global stuff
    private val totalButtons = 3
    private var i = 0
    private val head = LinkNode(
        0, 0, "Game/Series Title | Release Year | Console" +
                "\n-- Acquired -- Beaten -- One Hundred Percent Completion--", -1
    )

    private var paddingPx = 0f
    private var buttonPadding = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            saveData()
            Snackbar.make(view, "Saving...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val paddingDp = 10f
        paddingPx = android.util.TypedValue.applyDimension(
            android.util.TypedValue.COMPLEX_UNIT_DIP,
            paddingDp,
            applicationContext.resources.displayMetrics
        )
        buttonPadding = paddingPx.toInt()
        loadData()
    }

    private fun loadData() {
        val fileName = "gamelist.save"
        val file = File(filesDir.absolutePath, fileName)
        //Save File is present, load from that
        if (file.exists()) {
            val myInputStream = openFileInput(fileName)
            val myInputReader = InputStreamReader(myInputStream)
            val inputBuffer = CharArray(1)
            var bytes = 0
            var firstNum: Int
            var secondNum: Int
            var buttonNum = 0
            var button1State = 0
            var button2State = 0
            var button3State = 0

            var newNodeSKey: Int
            var newNodeGKey: Int
            var newGameText: String

            while (bytes != -1) {
                newGameText = ""
                bytes = myInputReader.read(inputBuffer)
                if (inputBuffer[0] == '\r') {
                    break
                }
                if (inputBuffer[0] == '\n') {
                    break
                }
                firstNum = inputBuffer[0] - '0'
                myInputReader.read(inputBuffer)
                if (inputBuffer[0] != ' ') {
                    secondNum = inputBuffer[0] - '0'
                    newNodeSKey = (firstNum * 10) + secondNum
                    myInputReader.read(inputBuffer)
                } else {
                    newNodeSKey = firstNum
                }
                myInputReader.read(inputBuffer)
                firstNum = inputBuffer[0] - '0'
                myInputReader.read(inputBuffer)
                if (inputBuffer[0] != ' ') {
                    secondNum = inputBuffer[0] - '0'
                    newNodeGKey = (firstNum * 10) + secondNum
                    myInputReader.read(inputBuffer)
                } else {
                    newNodeGKey = firstNum
                }
                if (newNodeGKey != 0) {
                    myInputReader.read(inputBuffer)
                    button1State = inputBuffer[0] - '0'
                    myInputReader.read(inputBuffer)
                    button2State = inputBuffer[0] - '0'
                    myInputReader.read(inputBuffer)
                    button3State = inputBuffer[0] - '0'
                }
                while (true) {
                    myInputReader.read(inputBuffer)
                    if (inputBuffer[0] == '\n') {
                        break
                    }
                    newGameText += inputBuffer[0]
                }
                if (newNodeGKey != 0) {
                    addNode(head, newNodeSKey, newNodeGKey, newGameText, buttonNum)
                    loadButtonState(head, buttonNum, button1State, button2State, button3State)
                    buttonNum += 3
                } else {
                    addNode(head, newNodeSKey, newNodeGKey, newGameText, -1)
                }
            }
            myInputStream.close()
            myInputReader.close()
            //Setup Vars
            buttonNum = 0
            var j = 0
            var headFlag = false
            var currentNode: LinkNode = head

            while (currentNode.nextNode != null) {
                if (headFlag) {
                    currentNode = currentNode.nextNode!!
                }
                //TableRow Code
                val tr = TableRow(this)
                tr.id = j + 1
                val trParams = TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )
                trParams.setMargins(
                    buttonPadding / 2, 5, buttonPadding / 2,
                    5
                )
                tr.setPadding(0, 0, 0, 0)
                tr.layoutParams = trParams
                //Text code
                val gameText = TextView(this)
                gameText.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                gameText.text = currentNode.gTit
                tr.addView(gameText)
                //Setup Table rows to be added and add them
                //Button Code
                if (currentNode == head) {
                    headFlag = true
                } else {
                    if (currentNode.gNum != 0) {
                        var k = 0
                        while (k < totalButtons) {
                            val button1 = Button(this)
                            button1.id = buttonNum
                            buttonNum++
                            button1.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                            //"#f7f7f7" for blank
                            if (k == 0) {
                                if (currentNode.button1 == 1) {
                                    val img2: Drawable =
                                        button1.context.resources.getDrawable(R.drawable.ic_check_box_black_24dp)
                                    button1.setCompoundDrawablesWithIntrinsicBounds(
                                        null,
                                        null,
                                        img2,
                                        null
                                    )
                                } else {
                                    val img: Drawable =
                                        button1.context.resources.getDrawable(R.drawable.ic_check_box_outline_blank_black_24dp)
                                    button1.setCompoundDrawablesWithIntrinsicBounds(
                                        null,
                                        null,
                                        img,
                                        null
                                    )
                                }
                            } else if (k == 1) {
                                if (currentNode.button2 == 1) {
                                    val img2: Drawable =
                                        button1.context.resources.getDrawable(R.drawable.ic_check_box_black_24dp)
                                    button1.setCompoundDrawablesWithIntrinsicBounds(
                                        null,
                                        null,
                                        img2,
                                        null
                                    )
                                } else {
                                    val img: Drawable =
                                        button1.context.resources.getDrawable(R.drawable.ic_check_box_outline_blank_black_24dp)
                                    button1.setCompoundDrawablesWithIntrinsicBounds(
                                        null,
                                        null,
                                        img,
                                        null
                                    )
                                }
                            } else {
                                if (currentNode.button3 == 1) {
                                    val img2: Drawable =
                                        button1.context.resources.getDrawable(R.drawable.ic_check_box_black_24dp)
                                    button1.setCompoundDrawablesWithIntrinsicBounds(
                                        null,
                                        null,
                                        img2,
                                        null
                                    )
                                } else {
                                    val img: Drawable =
                                        button1.context.resources.getDrawable(R.drawable.ic_check_box_outline_blank_black_24dp)
                                    button1.setCompoundDrawablesWithIntrinsicBounds(
                                        null,
                                        null,
                                        img,
                                        null
                                    )
                                }
                            }
                            button1.layoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                            button1.layoutParams.width = 90

                            //Button Code to make the button do stuff
                            button1.setOnClickListener {
                                Snackbar.make(button1, "PLEASE SAVE", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show()
                                val buttonState = updateButton(head, button1.id)
                                if (buttonState == 1) {
                                    val img2: Drawable =
                                        button1.context.resources.getDrawable(R.drawable.ic_check_box_black_24dp)
                                    button1.setCompoundDrawablesWithIntrinsicBounds(
                                        null,
                                        null,
                                        img2,
                                        null
                                    )
                                } else {
                                    val img3: Drawable =
                                        button1.context.resources.getDrawable(R.drawable.ic_check_box_outline_blank_black_24dp)
                                    button1.setCompoundDrawablesWithIntrinsicBounds(
                                        null,
                                        null,
                                        img3,
                                        null
                                    )
                                }
                                //End of Button Code
                            }
                            tr.addView(button1)
                            k++
                        }
                    }
                }
                j++
                layOut.addView(tr)
            }
        }
        //No Save File Present
        else {
            val myInputStream = assets.open("dataset.txt")
            val myInputReader = InputStreamReader(myInputStream)
            val inputBuffer = CharArray(1)
            var bytes = 0
            var firstNum: Int
            var secondNum: Int
            var buttonNum = 0
            var newNodeSKey: Int
            var newNodeGKey: Int
            var newGameText: String

            while (bytes != -1) {
                newGameText = ""
                bytes = myInputReader.read(inputBuffer)
                if (inputBuffer[0] == '\n') {
                    break
                }
                if (inputBuffer[0] == '\r') {
                    break
                }
                firstNum = inputBuffer[0] - '0'
                myInputReader.read(inputBuffer)
                if (inputBuffer[0] != ' ') {
                    secondNum = inputBuffer[0] - '0'
                    newNodeSKey = (firstNum * 10) + secondNum
                    myInputReader.read(inputBuffer)
                } else {
                    newNodeSKey = firstNum
                }
                myInputReader.read(inputBuffer)
                firstNum = inputBuffer[0] - '0'
                myInputReader.read(inputBuffer)
                if (inputBuffer[0] != ' ') {
                    secondNum = inputBuffer[0] - '0'
                    newNodeGKey = (firstNum * 10) + secondNum
                    myInputReader.read(inputBuffer)
                } else {
                    newNodeGKey = firstNum
                }
                while (true) {
                    myInputReader.read(inputBuffer)
                    if (inputBuffer[0] == '\r') {
                        myInputReader.read(inputBuffer)
                        break
                    }
                    if (inputBuffer[0] == '\n') {
                        break
                    }
                    newGameText += inputBuffer[0]
                }
                if (newNodeGKey != 0) {
                    addNode(head, newNodeSKey, newNodeGKey, newGameText, buttonNum)
                    buttonNum += 3
                } else {
                    addNode(head, newNodeSKey, newNodeGKey, newGameText, -1)
                }
            }
            myInputStream.close()
            myInputReader.close()

            //Setup Table rows to be added and add them
            //Setup Vars
            var headFlag = false
            buttonNum = 0
            var j = 0
            var currentNode: LinkNode = head

            while (currentNode.nextNode != null) {
                if (headFlag) {
                    currentNode = currentNode.nextNode!!
                }
                //TableRow Code
                val tr = TableRow(this)
                tr.id = j + 1
                val trParams = TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )
                trParams.setMargins(
                    buttonPadding, 5, buttonPadding,
                    5
                )
                tr.setPadding(0, 0, 0, 0)
                tr.layoutParams = trParams
                //Text code
                val gameText = TextView(this)
                gameText.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                gameText.text = currentNode.gTit
                tr.addView(gameText)
                //Button Code
                var k = 0
                if (currentNode == head) {
                    headFlag = true
                } else {
                    if (currentNode.gNum != 0) {
                        while (k < totalButtons) {
                            val button1 = Button(this)
                            button1.id = buttonNum
                            buttonNum++
                            button1.setBackgroundColor(Color.parseColor("#00FFFFFF"))
                            //"#f7f7f7" for blank
                            val img: Drawable =
                                button1.context.resources.getDrawable(R.drawable.ic_check_box_outline_blank_black_24dp)
                            button1.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null)
                            button1.layoutParams = TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                            button1.layoutParams.width = 90

                            //Button Code to make the button do stuff
                            button1.setOnClickListener {
                                Snackbar.make(button1, "PLEASE SAVE", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show()
                                val buttonState = updateButton(head, button1.id)
                                if (buttonState == 1) {
                                    val img2: Drawable =
                                        button1.context.resources.getDrawable(R.drawable.ic_check_box_black_24dp)
                                    button1.setCompoundDrawablesWithIntrinsicBounds(
                                        null,
                                        null,
                                        img2,
                                        null
                                    )
                                } else {
                                    val img3: Drawable =
                                        button1.context.resources.getDrawable(R.drawable.ic_check_box_outline_blank_black_24dp)
                                    button1.setCompoundDrawablesWithIntrinsicBounds(
                                        null,
                                        null,
                                        img3,
                                        null
                                    )
                                }
                                //End of Button Code
                            }
                            tr.addView(button1)
                            k++
                        }
                    }
                }

                j++
                layOut.addView(tr)
            }
        }
    }

    private fun saveData(): Int {
        val outPutStream = openFileOutput("gamelist.save", Context.MODE_PRIVATE)
        val outputWriter = OutputStreamWriter(outPutStream)
        var curNode: LinkNode = head
        while (curNode.nextNode != null) {
            curNode = curNode.nextNode!!
            if (curNode.gNum != 0) {
                outputWriter.write("${curNode.sNum}" + " " + "${curNode.gNum}" + " " + "${curNode.button1}" + "${curNode.button2}" + "${curNode.button3}" + curNode.gTit + '\n')
            } else {
                outputWriter.write("${curNode.sNum}" + " " + "${curNode.gNum}" + " " + curNode.gTit + '\n')
            }
            outputWriter.flush()
        }
        outputWriter.write("\n")
        outputWriter.write("\n")
        outputWriter.flush()
        outputWriter.close()
        outPutStream.close()
        return 1
    }

    //Linked List class to store game list in memory
    class LinkNode(seriesNum: Int, gameNum: Int, gameTitle: String, buttNum: Int) {
        val sNum = seriesNum
        val gNum = gameNum
        val gTit = gameTitle
        val buttonNum = buttNum
        var button1 = 0
        var button2 = 0
        var button3 = 0
        var nextNode: LinkNode? = null
    }

    private fun addNode(
        head: LinkNode,
        seriesKey: Int,
        gameKey: Int,
        gameText: String,
        buttonNum: Int
    ): Boolean {
        return if (head.nextNode == null) {
            head.nextNode = LinkNode(seriesKey, gameKey, gameText, buttonNum)
            true
        } else {
            val prev = searchNode(head, seriesKey, gameKey)
            if (prev != null) {
                val temp = LinkNode(seriesKey, gameKey, gameText, buttonNum)
                if (prev.nextNode == null) {
                    prev.nextNode = temp
                } else {
                    temp.nextNode = prev.nextNode
                    prev.nextNode = temp
                }
            }
            true
        }
    }

    private fun searchNode(head: LinkNode, seriesKey: Int, gameKey: Int): LinkNode? {
        var curNode: LinkNode = head
        if (curNode.nextNode == null) {
            return head
        } else {
            var i = 0
            var j = 0
            while (i <= seriesKey) {
                if (curNode.nextNode == null) {
                    return curNode
                }
                if (curNode.nextNode!!.sNum == seriesKey) {
                    while (j <= gameKey) {
                        if (curNode.nextNode == null) {
                            return curNode
                        }
                        if (curNode.nextNode!!.gNum >= gameKey) {
                            return curNode
                        }
                        if (curNode.nextNode!!.sNum > seriesKey) {
                            return curNode
                        }
                        curNode = curNode.nextNode!!
                        j = curNode.gNum
                    }
                    return curNode
                }
                if (curNode.nextNode!!.sNum > seriesKey) {
                    return curNode
                }
                curNode = curNode.nextNode!!
                i = curNode.sNum
            }
            //Really returns previous node to what we want
            return curNode
        }

    }

    private fun loadButtonState(
        head: LinkNode,
        buttonNum: Int,
        state1: Int,
        state2: Int,
        state3: Int
    ): Int {
        var curNode: LinkNode = head
        if (curNode.nextNode == null) {
            return -1
        }
        while (true) {
            if (curNode.nextNode == null) {
                break
            } else {
                curNode = curNode.nextNode!!
            }
            if (curNode.buttonNum == buttonNum) {
                curNode.button1 = state1
                curNode.button2 = state2
                curNode.button3 = state3
                return 1
            }
            i++
        }
        return -1
    }

    private fun updateButton(head: LinkNode, buttonNum: Int): Int {
        var curNode: LinkNode = head
        if (curNode.nextNode == null) {
            return -1
        }
        while (true) {
            if (curNode.nextNode == null) {
                break
            } else {
                curNode = curNode.nextNode!!
            }
            if (curNode.buttonNum == -1) {
                curNode = curNode.nextNode!!
                i++
            }
            if (curNode.buttonNum == buttonNum) {
                return if (curNode.button1 == 0) {
                    curNode.button1 = 1
                    1

                } else {
                    curNode.button1 = 0
                    0
                }
            }
            if (curNode.buttonNum + 1 == buttonNum) {
                return if (curNode.button2 == 0) {
                    curNode.button2 = 1
                    1
                } else {
                    curNode.button2 = 0
                    0
                }
            }
            if (curNode.buttonNum + 2 == buttonNum) {
                return if (curNode.button3 == 0) {
                    curNode.button3 = 1
                    1
                } else {
                    curNode.button3 = 0
                    0
                }
            }
            i++
        }
        return -1
    }

    fun importSaveFile(itemPressed: MenuItem) {
        val fileName = "gamelist.save"
        val file = File(filesDir.absolutePath, fileName)
        //If save file exists, overwrite it
        if (file.exists()) {
            val mEditText: EditText = findViewById(R.id.editText)
            val testText = mEditText.text.toString().trim()
            if (testText.isEmpty()) {
                Snackbar.make(mEditText, "Warning, can not make save blank", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                return
            }
            val outPutStream = openFileOutput("gamelist.save", Context.MODE_PRIVATE)
            val outputWriter = OutputStreamWriter(outPutStream)
            outputWriter.write(mEditText.text.toString())
            outputWriter.write("\n")
            outputWriter.write("\n")
            outputWriter.flush()
            outputWriter.close()
            outPutStream.close()
            Snackbar.make(
                    findViewById(itemPressed.groupId),
                    "Overwriting save file",
                    Snackbar.LENGTH_LONG
                )
                .setAction("Action", null).show()
        } else {
            Snackbar.make(
                    findViewById(itemPressed.groupId),
                    "Hit the Save Button First",
                    Snackbar.LENGTH_LONG
                )
                .setAction("Action", null).show()
        }
        return
    }

    fun exportSaveFile(itemPressed: MenuItem) {
        val mEditText: EditText = findViewById(R.id.editText)
        val fileName = "gamelist.save"
        val file = File(filesDir.absolutePath, fileName)
        //Save File is present, load from that
        if (file.exists()) {
            //Setup Input Stream
            val myInputStream = openFileInput(fileName)
            val myInputReader = InputStreamReader(myInputStream)
            val inputBuffer = CharArray(1)
            var newText = ""
            while (true) {
                myInputReader.read(inputBuffer)
                if (inputBuffer[0] == '\n') {
                    newText += '\n'
                    break
                }
                newText += inputBuffer[0]
                while (true) {
                    myInputReader.read(inputBuffer)
                    if (inputBuffer[0] == '\n') {
                        newText += '\n'
                        break
                    }
                    newText += inputBuffer[0]
                }
            }
            mEditText.setText(newText)
            myInputReader.close()
            myInputStream.close()
            Snackbar.make(
                    findViewById(itemPressed.groupId),
                    "Loaded save file contents for export",
                    Snackbar.LENGTH_LONG
                )
                .setAction("Action", null).show()
        } else {
            Snackbar.make(
                    findViewById(itemPressed.groupId),
                    "No Save File Present",
                    Snackbar.LENGTH_LONG
                )
                .setAction("Action", null).show()
        }
        return
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }
}