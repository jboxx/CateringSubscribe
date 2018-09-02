package io.github.jboxx.catering_subscribe.customviews;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class CustomRadioGroup {

    public interface OnButtonCheckedListener {
        void onButtonChecked(CompoundButton button, boolean isSetManually);
    }

    private final List<CompoundButton> buttons;
    private final View.OnClickListener onClick = v -> {
        setChecked(findCompoundButton(v), false);
    };

    private OnButtonCheckedListener listener;
    private CompoundButton lastChecked;

    public CustomRadioGroup() {
        buttons = new ArrayList<>();
    }

    public CustomRadioGroup(View view) {
        buttons = new ArrayList<>();
        parseView(view);
    }

    public void setView(View view) {
        parseView(view);
    }

    private CompoundButton findCompoundButton(View view) {
        if (view instanceof CompoundButton) {
            return (CompoundButton) view;
        } else if(view instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                parseView(group.getChildAt(i));
            }
        }
        return (CompoundButton) ((ViewGroup)view).getChildAt(0);
    }

    private void parseView(final View view) {
        if(view instanceof CompoundButton) {
            buttons.add((CompoundButton) view);
            ((LinearLayout)view.getParent()).setOnClickListener(onClick);
        } else if(view instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount();i++) {
                parseView(group.getChildAt(i));
            }
        }
    }

    public List<CompoundButton> getButtons() { return buttons; }

    public CompoundButton getLastChecked() { return lastChecked; }

    public void setChecked(int index, boolean isSetManual) { setChecked(buttons.get(index), isSetManual); }

    public void setChecked(CompoundButton button, boolean isSetManually) {
        if(button == lastChecked) return;

        for (CompoundButton btn : buttons) {
            btn.setChecked(false);
        }

        button.setChecked(true);

        lastChecked = button;

        if(listener != null) {
            listener.onButtonChecked(button, isSetManually);
        }
    }

    public void setOnButtonCheckedListener(OnButtonCheckedListener listener) { this.listener = listener; }
}
