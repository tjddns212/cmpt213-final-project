package group6.learnlock.ui.clock

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import group6.learnlock.R
import group6.learnlock.databinding.FragmentClockBinding
import java.util.*

class ClockFragment : Fragment() {

    private var _binding: FragmentClockBinding? = null
    private val binding get() = _binding!!

    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 3600000 // 60 minutes
    private var isTimerRunning: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClockBinding.inflate(inflater, container, false)

        binding.startButton.setOnClickListener {
            if (isTimerRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        binding.pauseButton.setOnClickListener {
            pauseTimer()
            binding.startButton.text = getString(R.string.resume)
        }

        updateCountDownText() // Initialize the timer display

        return binding.root
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                timerFinished()
            }
        }.start()

        isTimerRunning = true
        binding.startButton.text = getString(R.string.pause)
        binding.pauseButton.visibility = View.GONE
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        isTimerRunning = false
        binding.startButton.text = getString(R.string.start)
        binding.pauseButton.visibility = View.GONE
    }

    private fun timerFinished() {
        binding.timerTextView.text = getString(R.string.timer_default)
        showEndSessionPopup()
        isTimerRunning = false
        binding.startButton.text = getString(R.string.start)
        binding.pauseButton.visibility = View.GONE

    }

    private fun updateCountDownText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        binding.timerTextView.text = timeFormatted
    }

    private fun showEndSessionPopup() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.session_end_message))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                // User clicked OK button
            }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        _binding = null
    }
}
