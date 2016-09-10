package vijay.education.academylive.viewPager;

import vijay.education.academylive.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DialogFeedback extends android.app.Dialog {

    Context context;
    View view;
    View backView;
    String message;
    TextView messageTextView;
    String title;
    TextView titleTextView;
    RadioGroup radioGroup;
    RadioButton msg1, msg2, msg3, msg4,msg5;

    ButtonFlat buttonAccept;
    ButtonFlat buttonCancel;

    String buttonCancelText;

    View.OnClickListener onAcceptButtonClickListener;
    View.OnClickListener onCancelButtonClickListener;


    public DialogFeedback(Context context, String title, String message) {
        super(context, android.R.style.Theme_Translucent);
        this.context = context;// init Context
        this.message = message;
        this.title = title;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_feedback);

        view = (RelativeLayout) findViewById(R.id.contentDialog2);
        backView = (RelativeLayout) findViewById(R.id.dialog_rootView2);
        backView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getX() < view.getLeft()
                        || event.getX() > view.getRight()
                        || event.getY() > view.getBottom()
                        || event.getY() < view.getTop()) {
                    dismiss();
                }
                return false;
            }
        });

        this.titleTextView = (TextView) findViewById(R.id.title2);
        setTitle(title);

        this.messageTextView = (TextView) findViewById(R.id.message2);
        setMessage(message);

        msg1 = (RadioButton) findViewById(R.id.msg1);
        msg2 = (RadioButton) findViewById(R.id.msg2);
        msg3 = (RadioButton) findViewById(R.id.msg3);
        msg4 = (RadioButton) findViewById(R.id.msg4);
        msg5 = (RadioButton) findViewById(R.id.msg5);

        this.buttonAccept = (ButtonFlat) findViewById(R.id.button_accept);
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onAcceptButtonClickListener != null)
                    onAcceptButtonClickListener.onClick(v);
                int selectedId = radioGroup.getCheckedRadioButtonId();

                if (selectedId == msg1.getId()) {
                    setMessage("You chose 'msg1' option");
                } else if (selectedId == msg2.getId()) {
                    setMessage("You chose 'msg2' option");
                } else if (selectedId == msg3.getId()) {
                    setMessage("You chose 'msg3' option");
                } else if(selectedId == msg4.getId()){
                    setMessage("You chose 'msg4' option");
                }else {
                    setMessage("You chose 'msg5' option");
                }
                dismiss();
            }
        });
        //  this.buttonCancel = (ButtonFlat) findViewById(R.id.button_cancel);
       /* buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onCancelButtonClickListener != null)
                    onCancelButtonClickListener.onClick(v);
            }
        });*/
        this.radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                SharedPreferences.Editor editor = getContext().getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE).edit();

                if (checkedId == R.id.msg1) {
                    editor.putString("feedback", msg1.getText().toString());
                } else if (checkedId == R.id.msg2) {
                    editor.putString("feedback", msg2.getText().toString());
                } else if (checkedId == R.id.msg3) {
                    editor.putString("feedback", msg3.getText().toString());
                } else if (checkedId == R.id.msg4) {
                    editor.putString("feedback", msg4.getText().toString());
                }else {
                    editor.putString("feedback", msg5.getText().toString());
                    //Toast.makeText(getContext(), "choice: msg5",
                    //        Toast.LENGTH_SHORT).show();
                }
                editor.commit();
            }

        });

        /*if (buttonCancelText != null) {
            this.buttonCancel = (ButtonFlat) findViewById(R.id.button_cancel);
            this.buttonCancel.setVisibility(View.VISIBLE);
            this.buttonCancel.setText(buttonCancelText);
            buttonCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                    if (onCancelButtonClickListener != null)
                        onCancelButtonClickListener.onClick(v);
                }
            });
        }*/

    }

    @Override
    public void show() {

        super.show();
        // set dialog_rate_us enter animations
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_main_show_amination));
        backView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_root_show_amin));

    }

    // GETERS & SETTERS

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        messageTextView.setText(message);
    }

    public TextView getMessageTextView() {
        return messageTextView;
    }

    public void setMessageTextView(TextView messageTextView) {
        this.messageTextView = messageTextView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if (title == null)
            titleTextView.setVisibility(View.GONE);
        else {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(title);
        }
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public void setTitleTextView(TextView titleTextView) {
        this.titleTextView = titleTextView;
    }

    public Button getButtonAccept() {
        return buttonAccept;
    }

    public void setButtonAccept(ButtonFlat buttonAccept) {
        this.buttonAccept = buttonAccept;
    }

    public ButtonFlat getButtonCancel() {
        return buttonCancel;
    }

    public void setButtonCancel(ButtonFlat buttonCancel) {
        this.buttonCancel = buttonCancel;
    }

    public void setOnAcceptButtonClickListener(
            View.OnClickListener onAcceptButtonClickListener) {
        this.onAcceptButtonClickListener = onAcceptButtonClickListener;
        if (buttonAccept != null)
            buttonAccept.setOnClickListener(onAcceptButtonClickListener);
    }

    public void setOnCancelButtonClickListener(
            View.OnClickListener onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        if (buttonCancel != null)
            buttonCancel.setOnClickListener(onCancelButtonClickListener);
    }

    @Override
    public void dismiss() {

        Animation anim = AnimationUtils.loadAnimation(context, R.anim.dialog_main_hide_amination);
        anim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        DialogFeedback.super.dismiss();
                    }
                });

            }
        });
        Animation backAnim = AnimationUtils.loadAnimation(context, R.anim.dialog_root_hide_amin);

        view.startAnimation(anim);
        backView.startAnimation(backAnim);
        super.dismiss();
    }


}
