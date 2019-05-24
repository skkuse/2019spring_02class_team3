package edu.skku.swe042_team03.mysecretdairy

import android.os.Bundle
import android.support.constraint.motion.MotionLayout
import android.support.v7.app.AppCompatActivity
import android.view.View

class CalenderView : AppCompatActivity() {
    private var selectedIndex: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calender)

        val motionLayout = findViewById<MotionLayout>(R.id.motion_container)

        val v1 = findViewById<View>(R.id.v1)
        val v2 = findViewById<View>(R.id.v2)
        val v3 = findViewById<View>(R.id.v3)
        val v4 = findViewById<View>(R.id.v4)
        val v5 = findViewById<View>(R.id.v5)
        val v6 = findViewById<View>(R.id.v6)
        val v7 = findViewById<View>(R.id.v7)
        val v8 = findViewById<View>(R.id.v8)
        val v9 = findViewById<View>(R.id.v9)
        val v10 = findViewById<View>(R.id.v10)
        val v11 = findViewById<View>(R.id.v11)
        val v12 = findViewById<View>(R.id.v12)

        v1.setOnClickListener {
            if (selectedIndex == 0) return@setOnClickListener

            motionLayout.setTransition(R.id.s2, R.id.s1) //2 to 1 transition
            motionLayout.transitionToEnd()
            selectedIndex = 0;
        }
        v2.setOnClickListener {
            if (selectedIndex == 1) return@setOnClickListener

            if (selectedIndex == 2) {
                motionLayout.setTransition(R.id.s3, R.id.s2)  //3 to 2 transition
            } else {
                motionLayout.setTransition(R.id.s1, R.id.s2) //1 to 2 transition
            }
            motionLayout.transitionToEnd()
            selectedIndex = 1;
        }
        v3.setOnClickListener {
            if (selectedIndex == 2) return@setOnClickListener

            if (selectedIndex == 3) {
                motionLayout.setTransition(R.id.s4, R.id.s3)  //4 to 3 transition
            } else {
                motionLayout.setTransition(R.id.s2, R.id.s3) //2 to 3 transition
            }
            motionLayout.transitionToEnd()
            selectedIndex = 2;
        }
        v4.setOnClickListener {
            if (selectedIndex == 3) return@setOnClickListener

            if (selectedIndex == 4) {
                motionLayout.setTransition(R.id.s5, R.id.s4)  //5 to 4 transition
            } else {
                motionLayout.setTransition(R.id.s3, R.id.s4) //3 to 4 transition
            }
            motionLayout.transitionToEnd()
            selectedIndex = 3;
        }
        v5.setOnClickListener {
            if (selectedIndex == 4) return@setOnClickListener

            if (selectedIndex == 5) {
                motionLayout.setTransition(R.id.s6, R.id.s5)  //6 to 5 transition
            } else {
                motionLayout.setTransition(R.id.s4, R.id.s5) //4 to 5 transition
            }
            motionLayout.transitionToEnd()
            selectedIndex = 4;
        }
        v6.setOnClickListener {
            if (selectedIndex == 5) return@setOnClickListener

            if (selectedIndex == 6) {
                motionLayout.setTransition(R.id.s7, R.id.s6)  //7 to 6 transition
            } else {
                motionLayout.setTransition(R.id.s5, R.id.s6) //5 to 6 transition
            }
            motionLayout.transitionToEnd()
            selectedIndex = 5;
        }
        v7.setOnClickListener {
            if (selectedIndex == 6) return@setOnClickListener

            if (selectedIndex == 7) {
                motionLayout.setTransition(R.id.s8, R.id.s7)  //8 to 7 transition
            } else {
                motionLayout.setTransition(R.id.s6, R.id.s7) //6 to 7 transition
            }
            motionLayout.transitionToEnd()
            selectedIndex = 6;
        }
        v8.setOnClickListener {
            if (selectedIndex == 7) return@setOnClickListener

            if (selectedIndex == 8) {
                motionLayout.setTransition(R.id.s9, R.id.s8)  //9 to 8 transition
            } else {
                motionLayout.setTransition(R.id.s7, R.id.s8) //7 to 8 transition
            }
            motionLayout.transitionToEnd()
            selectedIndex = 7;
        }
        v9.setOnClickListener {
            if (selectedIndex == 8) return@setOnClickListener

            if (selectedIndex == 9) {
                motionLayout.setTransition(R.id.s10, R.id.s9)  //10 to 9 transition
            } else {
                motionLayout.setTransition(R.id.s8, R.id.s9) //8 to 9 transition
            }
            motionLayout.transitionToEnd()
            selectedIndex = 8;
        }
        v10.setOnClickListener {
            if (selectedIndex == 9) return@setOnClickListener

            if (selectedIndex == 10) {
                motionLayout.setTransition(R.id.s11, R.id.s10)  //11 to 10 transition
            } else {
                motionLayout.setTransition(R.id.s9, R.id.s10) //9 to 10 transition
            }
            motionLayout.transitionToEnd()
            selectedIndex = 9;
        }
        v11.setOnClickListener {
            if (selectedIndex == 10) return@setOnClickListener

            if (selectedIndex == 11) {
                motionLayout.setTransition(R.id.s12, R.id.s11)  //12 to 11 transition
            } else {
                motionLayout.setTransition(R.id.s10, R.id.s11) //10 to 11 transition
            }
            motionLayout.transitionToEnd()
            selectedIndex = 10;
        }
        v12.setOnClickListener {
            if (selectedIndex == 11) return@setOnClickListener

            motionLayout.setTransition(R.id.s11, R.id.s12) //11 to 12 transition
            motionLayout.transitionToEnd()
            selectedIndex = 11;
        }
    }
}
