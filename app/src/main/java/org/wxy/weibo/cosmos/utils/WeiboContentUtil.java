package org.wxy.weibo.cosmos.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;

import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import org.wxy.weibo.cosmos.Constants;
import org.wxy.weibo.cosmos.MainActivity;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.ui.activity.UserShowActivity;
import org.wxy.weibo.cosmos.ui.activity.WebActivity;

import java.util.regex.Matcher;

/**
 * Created by wxy on 2018/7/5.
 */

public class WeiboContentUtil {
    public static SpannableStringBuilder Weibocontent(String source, TextView textView){
        int textsize=(int)textView.getTextSize();
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableStringBuilder builder=new SpannableStringBuilder(source);
        Linkify.addLinks(builder, Constants.PATTERN_TOPIC,Constants.SCHEME_TOPIC);
        Linkify.addLinks(builder, Constants.PATTERN_URL,Constants.SCHEME_URL);
        Linkify.addLinks(builder, Constants.PATTERN_AT,Constants.SCHEME_AT);
        WeiboClickableSpan clickableSpan;
        URLSpan[] urlSpans =builder.getSpans(0,builder.length(),URLSpan.class);

        for (final URLSpan urlSpan:urlSpans) {
            clickableSpan = new WeiboClickableSpan() {
                @Override
                public void onClick(View view) {
                    if (urlSpan.getURL().indexOf(Constants.SCHEME_URL)!=-1)
                    new WebActivity().getUrl(MainActivity.mainActivity(),urlSpan.getURL().replace("url:",""));
                    if (urlSpan.getURL().indexOf(Constants.SCHEME_AT)!=-1)
                        new UserShowActivity().getName(MainActivity.mainActivity(),urlSpan.getURL().replace("at:@",""));
                }
            };
            if (urlSpan.getURL().startsWith(Constants.SCHEME_TOPIC))
            {
                int start =builder.getSpanStart(urlSpan);
                int end=builder.getSpanEnd(urlSpan);
                builder.removeSpan(urlSpan);
                builder.setSpan(clickableSpan,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (urlSpan.getURL().startsWith(Constants.SCHEME_URL))
            {
                int start =builder.getSpanStart(urlSpan);
                int end=builder.getSpanEnd(urlSpan);
                builder.removeSpan(urlSpan);
                SpannableStringBuilder stringBuilder = getUrlTextSpannableString(MainActivity.mainActivity(),urlSpan.getURL(),textsize);
                builder.replace(start,end,stringBuilder);
                builder.setSpan(clickableSpan,start,start+stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (urlSpan.getURL().startsWith(Constants.SCHEME_AT))
            {
                int start =builder.getSpanStart(urlSpan);
                int end=builder.getSpanEnd(urlSpan);
                builder.removeSpan(urlSpan);
                builder.setSpan(clickableSpan,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        Matcher matcher=Constants.PATTERN_EMOTION.matcher(builder);
        while (matcher.find())
        {
            String emjoy=matcher.group();
            int start=matcher.start();
            int end=matcher.end();
            int resid=EmjoyUtil.getImageByName(emjoy);
            if (resid!=-1)
            {
                Drawable drawable=MainActivity.mainActivity().getResources().getDrawable(resid);
                drawable.setBounds(0,0,(int)(textsize*1.3),(int)(textsize*1.3));
                VerticalImageSpan span=new VerticalImageSpan(drawable);
                builder.setSpan(span,start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return builder;
    }

    public static SpannableStringBuilder getUrlTextSpannableString(Context context,String source,int size){
        SpannableStringBuilder builder=new SpannableStringBuilder(source);
        String prefix=" ";
        builder.replace(0,prefix.length(),prefix);
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_sending);
        drawable.setBounds(0,0,size,size);
        builder.setSpan(new VerticalImageSpan(drawable),prefix.length(),source.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append("网页链接");
        return builder;
    }
    public static class VerticalImageSpan extends ImageSpan {

        public VerticalImageSpan(Drawable drawable) {
            super(drawable);
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end,
                           Paint.FontMetricsInt fontMetricsInt) {
            Drawable drawable = getDrawable();
            Rect rect = drawable.getBounds();
            if (fontMetricsInt != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.descent - fmPaint.ascent;
                int drHeight = rect.bottom - rect.top;
                int centerY = fmPaint.ascent + fontHeight / 2;

                fontMetricsInt.ascent = centerY - drHeight / 2;
                fontMetricsInt.top = fontMetricsInt.ascent;
                fontMetricsInt.bottom = centerY + drHeight / 2;
                fontMetricsInt.descent = fontMetricsInt.bottom;
            }
            return rect.right;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end,
                         float x, int top, int y, int bottom, Paint paint) {

            Drawable drawable = getDrawable();
            canvas.save();
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.descent - fmPaint.ascent;
            int centerY = y + fmPaint.descent - fontHeight / 2;
            int transY = centerY - (drawable.getBounds().bottom - drawable.getBounds().top) / 2;
            canvas.translate(x, transY);
            drawable.draw(canvas);
            canvas.restore();
        }
    }
    public static class WeiboClickableSpan extends ClickableSpan {
        @Override
        public void onClick(View view) {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.parseColor("#42A8E4"));
            ds.setUnderlineText(false);
        }
    }
}
