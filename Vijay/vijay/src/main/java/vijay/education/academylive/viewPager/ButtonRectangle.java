package vijay.education.academylive.viewPager;

/**
 * @author Bhagyashri Burade: 05/05/2015
 *
 */

import vijay.education.academylive.R;
import vijay.education.academylive.utils.Utils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ButtonRectangle extends Button {

	TextView textButton;

	int paddingTop, paddingBottom, paddingLeft, paddingRight;

	public ButtonRectangle(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDefaultProperties();
	}

	@Override
	protected void setDefaultProperties() {
		super.minWidth = 80;
		super.minHeight = 36;
		super.background = R.drawable.background_button_rectangle;
		super.setDefaultProperties();
	}

	// Set atributtes of XML to View
	protected void setAttributes(AttributeSet attrs) {

		// Set background Color
		// Color by resource
		int bacgroundColor = attrs.getAttributeResourceValue(ANDROIDXML,
				"background", -1);
		if (bacgroundColor != -1) {
			setBackgroundColor(getResources().getColor(bacgroundColor));
		} else {
			// Color by hexadecimal
			background = attrs.getAttributeIntValue(ANDROIDXML, "background",
					-1);
			if (background != -1)
				setBackgroundColor(background);
		}

		// Set Padding
		//String value = attrs.getAttributeValue(ANDROIDXML, "padding");

		// Set text button
		String text = null;
		int textResource = attrs.getAttributeResourceValue(ANDROIDXML, "text",
				-1);
		if (textResource != -1) {
			text = getResources().getString(textResource);
		} else {
			text = attrs.getAttributeValue(ANDROIDXML, "text");
		}
		if (text != null) {
			textButton = new TextView(getContext());
			textButton.setText(text);
			textButton.setTextColor(Color.WHITE);
			textButton.setTypeface(null, Typeface.BOLD);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
			params.setMargins(Utils.dpToPx(5, getResources()),
					Utils.dpToPx(5, getResources()),
					Utils.dpToPx(5, getResources()),
					Utils.dpToPx(5, getResources()));
			textButton.setLayoutParams(params);
			addView(textButton);
			// FrameLayout.LayoutParams params = (LayoutParams)
			// textView.getLayoutParams();
			// params.width = getWidth();
			// params.gravity = Gravity.CENTER_HORIZONTAL;
			// // params.setMargins(paddingLeft, paddingTop, paddingRight,
			// paddingRight);
			// textView.setLayoutParams(params);

		}

		rippleSpeed = attrs.getAttributeFloatValue(MATERIALDESIGNXML,
				"rippleSpeed", Utils.dpToPx(6, getResources()));
	}

	Integer height;
	Integer width;

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// if(!txtCenter)
		// centrarTexto();
		super.onDraw(canvas);
		if (x != -1) {
			Rect src = new Rect(0, 0, getWidth()
					- Utils.dpToPx(6, getResources()), getHeight()
					- Utils.dpToPx(7, getResources()));
			Rect dst = new Rect(Utils.dpToPx(6, getResources()), Utils.dpToPx(
					6, getResources()), getWidth()
					- Utils.dpToPx(6, getResources()), getHeight()
					- Utils.dpToPx(7, getResources()));
			canvas.drawBitmap(makeCircle(), src, dst, null);
			invalidate();
		}
	}

	public void setText(String text) {
		textButton.setText(text);
	}

	public void setTextColor(int color) {
		textButton.setTextColor(color);
	}

	@Override
	public TextView getTextView() {
		return textButton;
	}

	public String getText() {
		return textButton.getText().toString();
	}

}
