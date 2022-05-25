package andlima.hafizhfy.estafet24mei

import andlima.hafizhfy.estafet24mei.datastore.UserManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        userManager.name.asLiveData().observe(this, { name ->
            userManager.username.asLiveData().observe(this, { username ->
                userManager.age.asLiveData().observe(this, { age ->
                    userManager.address.asLiveData().observe(this, { address ->

                        tv_name.text = name
                        tv_username.append(username)
                        tv_age.text = age
                        tv_address.text = address

                    })
                })
            })
        })

        btn_logout.setOnClickListener {
            GlobalScope.launch {
                userManager.clearData()
            }
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }
}