package de.akquinet.android.robojecttest;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.akquinet.android.marvin.ActivityTestCase;
import de.akquinet.android.roboject.annotations.InjectView;
import de.akquinet.android.robojecttest.activities.InjectViewTestActivity;

import static de.akquinet.android.roboject.Roboject.injectViews;
import static org.hamcrest.CoreMatchers.notNullValue;

public class InjectViewCustomTest extends ActivityTestCase<InjectViewTestActivity> {
    public InjectViewCustomTest(Class<InjectViewTestActivity> activityType) {
        super(InjectViewTestActivity.class);
    }

    public void testInjectViewsByMemberName() {
        ViewObject viewObject = new ViewObject();
        injectViews(viewObject, getActivity().findViewById(android.R.id.content));

        assertThat(viewObject.theTextView, notNullValue());
        assertThat(viewObject.theImageView, notNullValue());
        assertThat(viewObject.theLinearLayout, notNullValue());
    }

    public void testInjectViewsById() {
        ViewObject viewObject = new ViewObject();
        injectViews(viewObject, getActivity().findViewById(android.R.id.content));

        assertThat(viewObject.theTextViewExplicitId, notNullValue());
        assertThat(viewObject.theImageViewExplicitId, notNullValue());
        assertThat(viewObject.theLinearLayoutExplicitId, notNullValue());
    }
}

class ViewObject {
    @InjectView
    TextView theTextView;

    @InjectView
    ImageView theImageView;

    @InjectView
    LinearLayout theLinearLayout;

    @InjectView(R.id.theTextView)
    TextView theTextViewExplicitId;

    @InjectView(R.id.theImageView)
    ImageView theImageViewExplicitId;

    @InjectView(R.id.theLinearLayout)
    LinearLayout theLinearLayoutExplicitId;
}