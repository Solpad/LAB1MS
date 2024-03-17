package com.example.lab1ms

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab1ms.ui.theme.LAB1MSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            LAB1MSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
private fun MainScreen() {

    var answer by remember { mutableStateOf("") }

    Column {
        var matrixValues by remember { mutableStateOf(emptyArray<Array<Int>>()) }
        MatrixInput { matrix ->
            matrixValues = matrix
        }

        Button(
            onClick = { answer = onMaximinButtonClick(matrixValues) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Поиск Максимина")
        }

        Button(
            onClick = { answer = onMinimaxButtonClick(matrixValues) },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Поиск Минимакса")
        }
        Text(
            text = answer,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
private fun MatrixInput(onMatrixEntered: (Array<Array<Int>>) -> Unit) {
    val matrix = remember { Array(3) { Array(3) { mutableStateOf("") } } }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        for (i in 0 until 3) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                for (j in 0 until 3) {
                    TextField(
                        value = matrix[i][j].value,
                        onValueChange = { newValue ->
                            matrix[i][j].value = newValue
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                }
            }
        }
        val context = LocalContext.current

        Button(
            onClick = {
                onSaveMatrixButtonClick(onMatrixEntered, matrix, context)
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Ввести матрицу")
        }

        Button(
            onClick = {
                for (i in matrix.indices) {
                    for (j in 0 until matrix[i].size) {
                        matrix[i][j].value = ""
                    }
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Очистить матрицу")
        }
    }
}

private fun convertStringMatrixToIntMatrix(stringMatrix: Array<Array<Int>>): Array<IntArray> {
    return stringMatrix.map { row ->
        row.map { it }.toIntArray()
    }.toTypedArray()
}

private fun onSaveMatrixButtonClick(
    onMatrixEntered: (Array<Array<Int>>) -> Unit,
    matrix: Array<Array<MutableState<String>>>,
    context: Context,
) {
    try {
        val matrixValues = Array(3) { i ->
            Array(3) { j ->
                if (isOnlyDigit(matrix[i][j].value)) {
                    matrix[i][j].value.toInt()
                } else {
                    matrix[i][j].value = removeNonDigits(matrix[i][j].value)
                    removeNonDigits(matrix[i][j].value).toInt()
                }
            }
        }
        onMatrixEntered(matrixValues)
    } catch (e: Throwable) {
        Toast.makeText(context, "Что-то пошло не так ;c", Toast.LENGTH_LONG).show()
    }
}

private fun onMaximinButtonClick(matrix: Array<Array<Int>>): String {
    val resolveMatrix = convertStringMatrixToIntMatrix(matrix)
    return "Максимин заданной матрицы: ${maximin(resolveMatrix)}"
}

private fun onMinimaxButtonClick(matrix: Array<Array<Int>>): String {
    val resolveMatrix = convertStringMatrixToIntMatrix(matrix)
    return "Минимакс заданной матрицы: ${minimax(resolveMatrix)}"
}

private fun isOnlyDigit(word: String): Boolean {
    var isOnlyDigits = true
    for (element in word) {
        if (!element.isDigit()) {
            isOnlyDigits = false
        }
    }
    return isOnlyDigits

}

private fun removeNonDigits(input: String): String {
    return input.filter { it.isDigit() }
}

private fun findMax(arr: IntArray, size: Int): Int {
    var max = arr[0]
    for (i in 1 until size) {
        if (arr[i] > max) {
            max = arr[i]
        }
    }
    return max
}

private fun findMin(arr: IntArray, size: Int): Int {
    var min = arr[0]
    for (i in 1 until size) {
        if (arr[i] < min) {
            min = arr[i]
        }
    }
    return min
}

private fun maximin(matrix: Array<IntArray>): Int {
    val maxminValues = IntArray(matrix.size)
    for (i in matrix.indices) {
        var rowMin = matrix[i][0]
        for (j in 1 until matrix[i].size) {
            if (matrix[i][j] < rowMin) {
                rowMin = matrix[i][j]
            }
        }
        maxminValues[i] = rowMin
    }
    return findMax(maxminValues, matrix.size)
}

private fun minimax(matrix: Array<IntArray>): Int {
    val minmaxValues = IntArray(matrix[0].size)
    for (j in matrix[0].indices) {
        var colMax = matrix[0][j]
        for (i in 1 until matrix.size) {
            if (matrix[i][j] > colMax) {
                colMax = matrix[i][j]
            }
        }
        minmaxValues[j] = colMax
    }
    return findMin(minmaxValues, matrix[0].size)
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LAB1MSTheme {
        MainScreen()
    }
}

