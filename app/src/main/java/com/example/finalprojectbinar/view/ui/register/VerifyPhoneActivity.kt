package com.example.finalprojectbinar.view.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.finalprojectbinar.databinding.ActivityVerifyPhoneBinding
import com.example.finalprojectbinar.util.Enum
import com.example.finalprojectbinar.util.SharedPreferenceHelper
import com.example.finalprojectbinar.util.Status
import com.example.finalprojectbinar.view.fragments.bottomsheets.BottomSheetSuccessRegisterFragment
import com.example.finalprojectbinar.view.ui.login.LoginActivity
import com.example.finalprojectbinar.viewmodel.MyViewModel
import org.koin.android.ext.android.inject
import androidx.lifecycle.Observer
import com.example.finalprojectbinar.model.OTPRequest
import java.text.SimpleDateFormat
import java.util.*

class VerifyPhoneActivity : AppCompatActivity() {

    private var _binding: ActivityVerifyPhoneBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityVerifyPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SharedPreferenceHelper.init(this)
        val email = intent.getStringExtra(RegisterActivity.EMAIL)
        binding.emailTV.text = email
        val tokenRegister = SharedPreferenceHelper.read(Enum.PREF_REGISTER.value).toString()

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        validateJWT(tokenRegister)

        binding.btnOTPVerification.setOnClickListener {
            val otp = binding.otpTextView.text.toString()
            if (otp.isNotEmpty()) {
                validateRegister(tokenRegister, otp)
            } else {
                Toast.makeText(this@VerifyPhoneActivity, "Please enter the OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateJWT(tokenRegister: String?) {
        if (tokenRegister != null) {
            viewModel.validateJWT("Bearer $tokenRegister").observe(this, Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        val expiredAt = it.data?.data?.expiredAt
                        if (expiredAt != null) {
                            showExpiryTime(expiredAt)
                        }
                        Toast.makeText(this@VerifyPhoneActivity, "JWT Valid", Toast.LENGTH_SHORT).show()
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@VerifyPhoneActivity, "Invalid JWT", Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        Log.d("LoadingTEST", "Loading")
                    }
                }
            })
        } else {
            Toast.makeText(this@VerifyPhoneActivity, "Anda belum terdaftar!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@VerifyPhoneActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showExpiryTime(expiredAt: String) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val expiryDate = sdf.parse(expiredAt)
        val currentTime = Calendar.getInstance().time
        val difference = expiryDate.time - currentTime.time

        val minutes = (difference / (1000 * 60) % 60).toInt()
        val seconds = (difference / 1000 % 60).toInt()

        binding.validationReminder.text = "OTP expires in: $minutes minutes and $seconds seconds"
    }

    private fun validateRegister(tokenRegister: String?, otp: String) {
        // Convert OTP to string if not already
        val otpRequest = OTPRequest(otp)

        viewModel.validateRegister("Bearer $tokenRegister", otpRequest).observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    val message = it.data?.message ?: "Registration Successful"
                    Toast.makeText(this@VerifyPhoneActivity, message, Toast.LENGTH_SHORT).show()
                    val bottomSheetSuccessRegister = BottomSheetSuccessRegisterFragment()
                    bottomSheetSuccessRegister.show(supportFragmentManager, bottomSheetSuccessRegister.tag)
                }
                Status.ERROR -> {
                    val message = it.message ?: "Unknown error occurred"
                    Log.d("TESTREGISTERVALID", it.toString())
                    Log.d("OTP", otp)
                    Log.d("TOKEN", tokenRegister ?: "No Token")
                    Toast.makeText(this@VerifyPhoneActivity, "Invalid credentials: $message", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    Log.d("LoadingTEST", "Loading")
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}