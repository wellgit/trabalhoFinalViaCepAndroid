package br.com.trabalhofinalviacepandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import br.com.trabalhofinalviacepandroid.R
import br.com.trabalhofinalviacepandroid.dataclass.ViaCepResponse
import br.com.trabalhofinalviacepandroid.service.RetrofitInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class InitialViewModel : ViewModel() {

    private val _resposta = MutableLiveData<ViaCepResponse?>()
    val resposta : LiveData<ViaCepResponse?>
        get() = _resposta

    val txtLogradouro = MutableLiveData<String?>()
    val txtBairro = MutableLiveData<String?>()
    val txtLocalidade = MutableLiveData<String?>()
    val txtComplemento = MutableLiveData<String?>()
    val txtUF = MutableLiveData<String?>()
    val txtMessage = MutableLiveData<String?>()

    fun getCep(cep:String) {
        viewModelScope.launch {
            try {
                _resposta.value = RetrofitInstance.retrofit.getCep(cep).body()
                if(_resposta.value != null){
                    txtMessage.value = null
                    txtLogradouro.value = _resposta.value?.logradouro
                    txtBairro.value = _resposta.value?.bairro
                    txtLocalidade.value = _resposta.value?.localidade
                    txtComplemento.value = _resposta.value?.complemento
                    txtUF.value = _resposta.value?.uf
                }else{
                    txtMessage.value = "NENHUM RESULTADO ENCONTRADO PARA O CEP INFORMADO"
                }
            }catch (e: Exception){
                Log.i("ERRO_CONSULTA_CEP", e.printStackTrace().toString())
                txtMessage.value = "ERRO_CONSULTA_CEP: ${cep}"
            }
        }
    }
}