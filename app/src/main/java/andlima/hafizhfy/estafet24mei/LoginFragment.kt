package andlima.hafizhfy.estafet24mei

import andlima.hafizhfy.estafet24mei.datastore.UserManager
import andlima.hafizhfy.estafet24mei.model.GetUserResponseItem
import andlima.hafizhfy.estafet24mei.network.ApiClient
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class LoginFragment : Fragment() {

    lateinit var email: String
    lateinit var password: String

    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        // Check is user loggen in
        isLoggedIn()

        btn_login.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if (email != "" && password != "") {
                login(email, password)
            } else {
                Toast.makeText(requireContext(), "Please input all field", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun login(username: String, password: String) {
        ApiClient.instance.getLoginData(username)
            .enqueue(object : retrofit2.Callback<List<GetUserResponseItem>>{
                override fun onResponse(
                    call: Call<List<GetUserResponseItem>>,
                    response: Response<List<GetUserResponseItem>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body()?.isEmpty() == true) {
                            Toast.makeText(requireContext(), "Unknown user", Toast.LENGTH_SHORT).show()
                        } else {
                            when {
                                response.body()?.size!! > 1 -> {
                                    Toast.makeText(requireContext(), "Please input data correctly", Toast.LENGTH_LONG).show()
                                }
                                username != response.body()!![0].username -> {
                                    Toast.makeText(requireContext(), "Username not registered", Toast.LENGTH_SHORT).show()
                                }
                                password != response.body()!![0].password -> {
                                    Toast.makeText(requireContext(), "Wrong password", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    GlobalScope.launch {
                                        userManager.userLogin(
                                            response.body()!![0].id,
                                            response.body()!![0].username,
                                            response.body()!![0].password,
                                            response.body()!![0].name,
                                            response.body()!![0].umur.toString(),
                                            response.body()!![0].address
                                        )
                                    }

                                    Navigation.findNavController(view!!)
                                        .navigate(R.id.action_loginFragment_to_homeFragment)
                                }

                            }
                        }

                    } else {
                        Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<List<GetUserResponseItem>>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun isLoggedIn() {
        userManager.username.asLiveData().observe(this, { username ->
            userManager.password.asLiveData().observe(this, { password ->
                if (username != "" && password != "") {
                    Navigation.findNavController(view!!).navigate(R.id.action_loginFragment_to_homeFragment)
                }
            })
        })
    }
}