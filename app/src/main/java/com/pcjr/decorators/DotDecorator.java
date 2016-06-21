package com.pcjr.decorators;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Date;

/**
 * Created by Mario on 2016/6/15.
 */
public class DotDecorator implements DayViewDecorator {

    private CalendarDay date;

    public DotDecorator() {
        date = CalendarDay.today();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(6,Color.parseColor("#FF6602")));
    }


    public void setDate(Date date) {
        this.date = CalendarDay.from(date);
    }
}