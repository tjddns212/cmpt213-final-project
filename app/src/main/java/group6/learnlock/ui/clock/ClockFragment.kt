package group6.learnlock.ui.clock

import android.app.NotificationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import group6.learnlock.R
import group6.learnlock.databinding.FragmentClockBinding
import java.util.*

class ClockFragment : Fragment() {

    private var _binding: FragmentClockBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ClockViewModel by viewModels()

    private var countDownTimer: CountDownTimer? = null
    companion object {
        const val FOCUS_NOTIFICATION_ID = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClockBinding.inflate(inflater, container, false)

        setupTimer()

        binding.startButton.setOnClickListener {
            if (viewModel.isTimerRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        binding.pauseButton.setOnClickListener {
            pauseTimer()
            binding.startButton.text = getString(R.string.resume)
        }

        binding.pauseButton.visibility = View.GONE // Initially hide the pause button

        return binding.root
    }

    private fun setupTimer() {
        if (viewModel.isTimerRunning) {
            startTimer()
        } else {
            updateCountDownText()
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(viewModel.timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                viewModel.timeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                timerFinished()
            }
        }.start()

        viewModel.isTimerRunning = true
        binding.startButton.text = getString(R.string.pause)
        binding.pauseButton.visibility = View.VISIBLE
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        viewModel.isTimerRunning = false
        binding.startButton.text = getString(R.string.start)
        binding.pauseButton.visibility = View.GONE
    }

    private fun timerFinished() {
        viewModel.timeLeftInMillis = 0
        updateCountDownText()
        showEndSessionPopup()
        viewModel.isTimerRunning = false
        binding.startButton.text = getString(R.string.start)
        binding.pauseButton.visibility = View.GONE
    }

    private fun updateCountDownText() {
        val minutes = (viewModel.timeLeftInMillis / 1000) / 60
        val seconds = (viewModel.timeLeftInMillis / 1000) % 60
        val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        _binding?.timerTextView?.text = timeFormatted
    }

    private fun showEndSessionPopup() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.session_end_message))
            .setPositiveButton(getString(R.string.ok), null)
            .create()
            .show()
    }

    override fun onPause() {
        super.onPause()
        if (viewModel.isTimerRunning) {
            val notificationManager = requireActivity().getSystemService(NotificationManager::class.java)
            val notificationBuilder = NotificationCompat.Builder(requireContext(), getString(R.string.stay_focused_channel_id))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Stay Focused")
                .setContentText("Your focus timer is running. Stay focused!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
            notificationManager.notify(FOCUS_NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    override fun onResume() {
        super.onResume()
        val notificationManager = requireActivity().getSystemService(NotificationManager::class.java)
        notificationManager.cancel(FOCUS_NOTIFICATION_ID)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        _binding = null
    }
}
