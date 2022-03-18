package com.local.growkart.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seshin on 17.04.2017.
 */

public class SpannableUtil {

    public static void addClickablePart(SpannableString ss,
                                        String text, String word, int color,
                                        final ClickAction action) {

        if (word != null) {
            int idx1 = text.indexOf(word);
            if (idx1 == -1) {
                return;
            }
            int idx2 = idx1 + word.length();
            ss.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    action.action();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(color);
                }
            }, idx1, idx2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }


    public static void addMultipleColoredPart(SpannableString ss,
                                              String text, String word, int color) {
        List<Integer> indexList = new ArrayList<>();
        if (word != null) {
            int index = 0;
            while (index != -1) {
                index = text.indexOf(word, index == 0 ? 0 :index + word.length());
                if (index != -1) {
                    indexList.add(index);
                }
            }
            if (!indexList.isEmpty())
                for (int i = 0; i < indexList.size(); i++) {
                    int startIndex = indexList.get(i);
                    int endIndex = startIndex + word.length();
                    ss.setSpan(new ForegroundColorSpan(color), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
        }
    }

    public static void addColoredPart(SpannableString ss,
                                      String text, String word, int color) {

        if (word != null) {
            int idx1 = text.indexOf(word);
            if (idx1 == -1) {
                return;
            }
            int idx2 = idx1 + word.length();
            ss.setSpan(new ForegroundColorSpan(color), idx1, idx2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public static void addColoredPart(SpannableStringBuilder ssb,
                                      String text, String word, int color) {

        if (word != null) {
            int idx1 = text.indexOf(word);
            if (idx1 == -1) {
                return;
            }
            int idx2 = idx1 + word.length();
            ssb.setSpan(new ForegroundColorSpan(color), idx1, idx2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public static void addBoldPart(SpannableString ss,
                                   String text, String word) {

        if (word != null) {
            int idx1 = text.indexOf(word);
            if (idx1 == -1) {
                return;
            }
            int idx2 = idx1 + word.length();
            ss.setSpan(new StyleSpan(Typeface.BOLD), idx1, idx2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }


    public static void addUnderlinePart(SpannableString ss, String text, String underlineableText) {
        if (underlineableText != null) {
            int idx1 = text.indexOf(underlineableText);
            if (idx1 == -1) {
                return;
            }
            int idx2 = idx1 + underlineableText.length();
            ss.setSpan(new UnderlineSpan(), idx1, idx2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public static SpannableStringBuilder getImageSpanned(Context context, String text, int drawable) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        ImageSpan imageSpan = new ImageSpan(context, drawable, DynamicDrawableSpan.ALIGN_BASELINE);
        builder.setSpan(imageSpan, text.indexOf("icon"), text.indexOf("icon") + "icon".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public interface ClickAction {
        void action();
    }
}