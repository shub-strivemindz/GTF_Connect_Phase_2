package com.gtfconnect.utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.gtfconnect.R;

import kotlin.text.Typography;

public class TextViewUtil {

    public static void channelExpandableMessage(Context context, TextView messageTextView, String message,int limit_count, boolean isMessageSelfQuoted) {

        Spannable see_less,see_more;

        if (message.length() > limit_count) {
            String temp_message = message.substring(0, limit_count);

            see_more = new SpannableString(Html.fromHtml(temp_message + " ...see more"));
            see_less = new SpannableString(Html.fromHtml(message + " ...see less"));

            ClickableSpan clickableToggleSpan1 = new ClickableSpan() {
                @Override
                public void onClick(View textView) {

                    messageTextView.setText(see_more);
                    messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
                    messageTextView.setHighlightColor(Color.TRANSPARENT);

                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);

                    if (isMessageSelfQuoted) {
                        ds.setColor(context.getColor(R.color.quotedTextSelfExpandableColor));
                    }
                    else{
                        ds.setColor(context.getColor(R.color.theme_green));
                    }
                }
            };

            see_less.setSpan(clickableToggleSpan1, see_less.length() - 11, see_less.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            messageTextView.setText(see_less);
            messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
            messageTextView.setHighlightColor(Color.TRANSPARENT);






            ClickableSpan clickableToggleSpan2 = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    //messageTextView.setText(message);

                    messageTextView.setText(see_less);
                    messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
                    messageTextView.setHighlightColor(Color.TRANSPARENT);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);

                    if (isMessageSelfQuoted) {
                        ds.setColor(context.getColor(R.color.quotedTextSelfExpandableColor));
                    }
                    else{
                        ds.setColor(context.getColor(R.color.theme_green));
                    }
                }
            };

            see_more.setSpan(clickableToggleSpan2, see_more.length() - 11, see_more.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            messageTextView.setText(see_more);
            messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
            messageTextView.setHighlightColor(Color.TRANSPARENT);
        }
        else{
            messageTextView.setText(message);
        }



    }

    public static void groupExpandableMessage(Context context,TextView messageTextView,String message,int limit_count, boolean isMessageSelfQuoted) {

        Spannable see_less,see_more;

        if (message.length() > limit_count) {
            String temp_message = message.substring(0, limit_count);

            see_more = new SpannableString(Html.fromHtml(temp_message + " ...see more"));
            see_less = new SpannableString(Html.fromHtml(message + " ...see less"));

            ClickableSpan clickableToggleSpan1 = new ClickableSpan() {
                @Override
                public void onClick(View textView) {

                    messageTextView.setText(see_more);
                    messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
                    messageTextView.setHighlightColor(Color.TRANSPARENT);

                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    if (isMessageSelfQuoted) {
                        ds.setColor(context.getColor(R.color.quotedTextSelfExpandableColor));
                    }
                    else{
                        ds.setColor(context.getColor(R.color.theme_green));
                    }
                }
            };

            see_less.setSpan(clickableToggleSpan1, see_less.length() - 11, see_less.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            messageTextView.setText(see_less);
            messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
            messageTextView.setHighlightColor(Color.TRANSPARENT);






            ClickableSpan clickableToggleSpan2 = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    //messageTextView.setText(message);

                    messageTextView.setText(see_less);
                    messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
                    messageTextView.setHighlightColor(Color.TRANSPARENT);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    if (isMessageSelfQuoted) {
                        ds.setColor(context.getColor(R.color.quotedTextSelfExpandableColor));
                    }
                    else{
                        ds.setColor(context.getColor(R.color.theme_green));
                    }
                }
            };

            see_more.setSpan(clickableToggleSpan2, see_more.length() - 11, see_more.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            messageTextView.setText(see_more);
            messageTextView.setMovementMethod(LinkMovementMethod.getInstance());
            messageTextView.setHighlightColor(Color.TRANSPARENT);
        }
        else{
            messageTextView.setText(message);
        }



    }
}