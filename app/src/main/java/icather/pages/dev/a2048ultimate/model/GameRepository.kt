package icather.pages.dev.a2048ultimate.model

import android.content.Context
import android.content.SharedPreferences

class GameRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("2048_prefs", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_BEST_SCORE = "best_score"
    }

    fun getBestScore(): Long {
        return prefs.getLong(KEY_BEST_SCORE, 0)
    }

    fun saveBestScore(score: Long) {
        val currentBest = getBestScore()
        if (score > currentBest) {
            prefs.edit().putLong(KEY_BEST_SCORE, score).apply()
        }
    }
}
