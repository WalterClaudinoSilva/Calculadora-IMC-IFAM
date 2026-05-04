package br.com.roboticminds.calculadoradeimc_ifam

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * MainActivity é a tela principal do aplicativo Calculadora de IMC.
 * Esta classe é responsável por gerenciar a interface do usuário e a lógica
 * de cálculo do Índice de Massa Corporal (IMC).
 */
class MainActivity : AppCompatActivity() {

    /**
     * O método onCreate é chamado quando a Activity está iniciando.
     * É aqui que inicializamos a interface e configuramos as interações do usuário.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Ativa o layout de ponta a ponta (edge-to-edge), permitindo que o app use a área da barra de status e de navegação.
        enableEdgeToEdge()
        
        // Define o layout XML que será utilizado para esta tela (activity_main.xml)
        setContentView(R.layout.activity_main)
        
        // Ajusta o padding da view principal para não sobrepor a barra de status e a barra de navegação do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // --- Vinculando os componentes do Layout XML com as variáveis em Kotlin ---
        
        // Campo de texto para o usuário digitar o peso
        val editTextWeight = findViewById<EditText>(R.id.edittext_weight)
        // Campo de texto para o usuário digitar a idade
        val editTextAge = findViewById<EditText>(R.id.edittext_age)
        // Barra deslizante (SeekBar) para o usuário selecionar a altura
        val seekBarHeight = findViewById<SeekBar>(R.id.seek_height)
        // Texto que exibe o valor numérico da altura selecionada no SeekBar
        val textHeightValue = findViewById<TextView>(R.id.text_height_value)
        // Texto que exibirá o resultado do IMC calculado
        val textResult = findViewById<TextView>(R.id.text_result)
        // Botão para iniciar o cálculo do IMC
        val buttonCalculate = findViewById<Button>(R.id.button_calculate)
        // Botão para limpar todos os campos e resultados
        val buttonClear = findViewById<Button>(R.id.button_clear)

        // --- Configurando os Eventos (Listeners) das Views ---

        // Configura o evento para atualizar o valor da altura na tela sempre que o SeekBar for movido
        seekBarHeight.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            
            // Método chamado continuamente enquanto a barra está sendo movida
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // Atualiza o texto com o progresso atual seguido de "cm"
                textHeightValue.text = "$progress cm"
                // Torna o texto visível na tela
                textHeightValue.visibility = View.VISIBLE
            }

            // Método chamado quando o usuário toca na barra para começar a mover
            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            // Método chamado quando o usuário solta a barra
            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        // Configura o evento de clique para o botão "Calcular"
        buttonCalculate.setOnClickListener {
            try {
                // Obtém o peso digitado pelo usuário e converte de texto (String) para número decimal (Double)
                val weight = editTextWeight.text.toString().toDouble()
                
                // Obtém a idade digitada pelo usuário e converte para número inteiro (Int)
                val ageText = editTextAge.text.toString()
                if (ageText.isEmpty()) {
                    Toast.makeText(this, R.string.msg_invalid_age, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val age = ageText.toInt()
                
                // Obtém a altura selecionada no SeekBar (em centímetros) e converte para metros dividindo por 100
                val height = seekBarHeight.progress.toDouble() / 100

                // Verifica se a altura é maior que zero para evitar divisão por zero no cálculo
                if (height > 0) {
                    // Realiza a fórmula matemática do IMC: peso dividido pela altura ao quadrado
                    val imc = weight / (height * height)
                    
                    // Formata o resultado para exibir a idade e o IMC calculado
                    textResult.text = String.format("Idade: %d anos\nIMC: %.2f", age, imc)
                    // Torna o texto do resultado visível na tela
                    textResult.visibility = View.VISIBLE
                } else {
                    // Exibe uma mensagem de erro em um Toast caso a altura seja inválida (zero ou negativa)
                    Toast.makeText(this, R.string.msg_invalid_height, Toast.LENGTH_SHORT).show()
                }
            } catch (e: NumberFormatException) {
                // Captura a exceção caso o usuário não tenha digitado um peso válido (ex: campo vazio)
                // e exibe uma mensagem de erro utilizando um Toast
                Toast.makeText(this, R.string.msg_invalid_weight, Toast.LENGTH_SHORT).show()
            }
        }

        // Configura o evento de clique para o botão "Limpar"
        buttonClear.setOnClickListener {
            // Limpa o campo de peso e idade
            editTextWeight.setText("")
            editTextAge.setText("")
            // Retorna o SeekBar de altura para a posição inicial (zero)
            seekBarHeight.progress = 0
            // Limpa o texto do resultado
            textResult.text = ""
            
            // Oculta novamente os textos de valor da altura e do resultado final
            textHeightValue.visibility = TextView.GONE
            textResult.visibility = TextView.GONE
        }
    }
}