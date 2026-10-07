package top.niunaijun.blackboxa.view.twin

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class TwinHomeActivity : AppCompatActivity() {
    private lateinit var content: LinearLayout
    private lateinit var nav: LinearLayout
    private var selected = 0

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(10, 18, 18)
        window.navigationBarColor = Color.rgb(10, 18, 18)
        buildUi()
        showClones()
    }

    private fun buildUi() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(10, 18, 18))
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(24), dp(22), dp(24), dp(14))
        }
        header.addView(TextView(this).apply {
            text = "Twin"
            textSize = 30f
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
        })
        header.addView(TextView(this).apply {
            text = "Clone and manage your apps"
            textSize = 14f
            setTextColor(Color.rgb(150, 170, 168))
            setPadding(0, dp(3), 0, 0)
        })
        root.addView(header)

        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(6), dp(18), dp(12))
        }
        root.addView(content, LinearLayout.LayoutParams(-1, 0, 1f))

        nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(dp(8), dp(7), dp(8), dp(10))
            setBackgroundColor(Color.rgb(18, 32, 32))
        }
        root.addView(nav, LinearLayout.LayoutParams(-1, dp(78)))
        setContentView(root)
        buildNav()
    }

    private fun buildNav() {
        nav.removeAllViews()
        val items = listOf(
            "Clones" to android.R.drawable.ic_menu_view,
            "Messages" to android.R.drawable.ic_dialog_info,
            "Profile" to android.R.drawable.ic_menu_myplaces,
            "Settings" to android.R.drawable.ic_menu_preferences
        )

        items.forEachIndexed { index, itemData ->
            val selectedItem = selected == index
            val item = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(dp(4), dp(3), dp(4), dp(3))
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    selected = index
                    buildNav()
                    when (index) {
                        0 -> showClones()
                        1 -> showMessages()
                        2 -> showProfile()
                        3 -> showSettings()
                    }
                }
            }

            val icon = ImageView(this).apply {
                setImageResource(itemData.second)
                setColorFilter(
                    if (selectedItem) Color.rgb(255, 100, 82)
                    else Color.rgb(170, 190, 188)
                )
                contentDescription = itemData.first
            }
            item.addView(icon, LinearLayout.LayoutParams(dp(24), dp(24)))

            item.addView(TextView(this).apply {
                text = itemData.first
                textSize = 11f
                gravity = Gravity.CENTER
                setTextColor(
                    if (selectedItem) Color.rgb(255, 100, 82)
                    else Color.rgb(170, 190, 188)
                )
                setPadding(0, dp(3), 0, 0)
            })

            nav.addView(item, LinearLayout.LayoutParams(0, -1, 1f))
        }
    }

    private fun showClones() {
        content.removeAllViews()

        val add = ExtendedFloatingActionButton(this).apply {
            text = "Clone an app"
            icon = getDrawable(android.R.drawable.ic_input_add)
            setOnClickListener {
                startActivity(Intent(this@TwinHomeActivity, PickerActivity::class.java))
            }
        }
        content.addView(add, LinearLayout.LayoutParams(-1, dp(54)).apply {
            setMargins(0, 0, 0, dp(16))
        })

        content.addView(card(
            "Clone status",
            "Ready. Select an installed app to create a virtual copy."
        ))

        content.addView(card(
            "Improved clone diagnostics",
            "Clone failures now report the actual installation error instead of only “Couldn't clone”."
        ), LinearLayout.LayoutParams(-1, -2).apply {
            topMargin = dp(12)
        })
    }

    private fun showMessages() {
        content.removeAllViews()
        content.addView(card(
            "Messages",
            "Clone activity and installation errors will appear here."
        ))
    }

    private fun showProfile() {
        content.removeAllViews()
        content.addView(card(
            "This device",
            "Android " + android.os.Build.VERSION.RELEASE +
                "\nAvailable heap: " + (Runtime.getRuntime().maxMemory() / 1024 / 1024) + " MB"
        ))
    }

    private fun showSettings() {
        content.removeAllViews()
        content.addView(card(
            "Settings",
            "Twin uses the BlackBox virtual environment for cloned apps."
        ))
    }

    private fun card(title: String, body: String): View {
        val card = MaterialCardView(this).apply {
            radius = dp(20).toFloat()
            setCardBackgroundColor(Color.rgb(20, 37, 37))
            strokeWidth = dp(1)
            strokeColor = Color.rgb(30, 52, 51)
        }

        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(16), dp(18), dp(16))
        }

        box.addView(TextView(this).apply {
            text = title
            textSize = 17f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
        })

        box.addView(TextView(this).apply {
            text = body
            textSize = 13f
            setTextColor(Color.rgb(170, 188, 186))
            setPadding(0, dp(7), 0, 0)
        })

        card.addView(box)
        return card
    }

    override fun onResume() {
        super.onResume()
        if (::content.isInitialized && selected == 0) showClones()
    }
}
