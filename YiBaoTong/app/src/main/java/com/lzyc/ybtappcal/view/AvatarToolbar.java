package com.lzyc.ybtappcal.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzyc.ybtappcal.R;

/**
 * 文本、图片跟随动画
 * Created by yang on 2017/4/12.
 */
public class AvatarToolbar extends LinearLayout implements AppBarLayout.OnOffsetChangedListener {

    private View avatarView;
    private TextView titleView;

    private float collapsedPadding;
    private float expandedPadding;

    private float expandedImageSize;
    private float collapsedImageSize;

    private float collapsedTextSize;
    private float expandedTextSize;

    private boolean valuesCalculatedAlready = false;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private float collapsedHeight;
    private float expandedHeight;
    private float maxOffset;

    public AvatarToolbar(Context context) {
        this(context, null);
        init();
    }

    public AvatarToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AvatarToolbar, 0, 0);
        try {
            collapsedPadding = a.getDimension(R.styleable.AvatarToolbar_collapsedPadding, -1);
            expandedPadding = a.getDimension(R.styleable.AvatarToolbar_expandedPadding, -1);
            collapsedImageSize = a.getDimension(R.styleable.AvatarToolbar_collapsedImageSize, -1);
            expandedImageSize = a.getDimension(R.styleable.AvatarToolbar_expandedImageSize, -1);
            collapsedTextSize = a.getDimension(R.styleable.AvatarToolbar_collapsedTextSize, -1);
            expandedTextSize = a.getDimension(R.styleable.AvatarToolbar_expandedTextSize, -1);
        } finally {
            a.recycle();
        }
        final Resources resources = getResources();
        if (collapsedPadding < 0) {
            collapsedPadding = resources.getDimension(R.dimen.default_collapsed_padding);
        }
        if (expandedPadding < 0) {
            expandedPadding = resources.getDimension(R.dimen.default_expanded_padding);
        }
        if (collapsedImageSize < 0) {
            collapsedImageSize = resources.getDimension(R.dimen.default_collapsed_image_size);
        }
        if (expandedImageSize < 0) {
            expandedImageSize = resources.getDimension(R.dimen.default_expanded_image_size);
        }
        if (collapsedTextSize < 0) {
            collapsedTextSize = resources.getDimension(R.dimen.default_collapsed_text_size);
        }
        if (expandedTextSize < 0) {
            expandedTextSize = resources.getDimension(R.dimen.default_expanded_text_size);
        }
    }

    private void init() {
        setOrientation(HORIZONTAL);
    }

    @NonNull
    private AppBarLayout findParentAppBarLayout() {
        ViewParent parent = this.getParent();
        if (parent instanceof AppBarLayout) {
            return ((AppBarLayout) parent);
        } else if (parent.getParent() instanceof AppBarLayout) {
            return ((AppBarLayout) parent.getParent());
        } else {
            throw new IllegalStateException("Must be inside an AppBarLayout"); //TODO actually, a collapsingtoolbar
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        findViews();
        if (!isInEditMode()) {
            appBarLayout.addOnOffsetChangedListener(this);
        } else {
            setExpandedValuesForEditMode();
        }
    }

    private void setExpandedValuesForEditMode() {
        calculateValues();
        updateViews(1f, 0);
    }

    private void findViews() {
        appBarLayout = findParentAppBarLayout();
        toolbar = findSiblingToolbar();
        avatarView = findAvatar();
        titleView = findTitle();
    }

    @NonNull
    private View findAvatar() {
        View avatar = this.findViewById(R.id.avatar);
        if (avatar == null) {
            throw new IllegalStateException("View with id ta_avatar not found");
        }
        return avatar;
    }

    @NonNull
    private TextView findTitle() {
        TextView title = (TextView) this.findViewById(R.id.title);
        if (title == null) {
            throw new IllegalStateException("TextView with id ta_title not found");
        }
        return title;
    }

    @NonNull
    private Toolbar findSiblingToolbar() {
        ViewGroup parent = ((ViewGroup) this.getParent());
        for (int i = 0, c = parent.getChildCount(); i < c; i++) {
            View child = parent.getChildAt(i);
            if (child instanceof Toolbar) {
                return (Toolbar) child;
            }
        }
        throw new IllegalStateException("No toolbar found as sibling");
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        if (!valuesCalculatedAlready) {
            calculateValues();
            valuesCalculatedAlready = true;
        }
        float expandedPercentage = 1 - (-offset / maxOffset);
        updateViews(expandedPercentage, offset);
    }

    private void calculateValues() {
        collapsedHeight = toolbar.getHeight();
        expandedHeight = appBarLayout.getHeight() - toolbar.getHeight();
        maxOffset = expandedHeight;
    }

    private void updateViews(float expandedPercentage, int currentOffset) {
        float inversePercentage = 1 - expandedPercentage;
        float translation = -currentOffset + ((float) toolbar.getHeight() * expandedPercentage);
        float currHeight = collapsedHeight + (expandedHeight - collapsedHeight) * expandedPercentage;
        float currentPadding = expandedPadding + (collapsedPadding - expandedPadding) * inversePercentage;
        float currentImageSize = collapsedImageSize + (expandedImageSize - collapsedImageSize) * expandedPercentage;
        float currentTextSize = collapsedTextSize + (expandedTextSize - collapsedTextSize) * expandedPercentage;

        setContainerOffset(translation);
        setContainerHeight((int) currHeight);
        setPadding((int) currentPadding);
        setAvatarSize((int) currentImageSize);
        setTextSize(currentTextSize);
    }

    private void setContainerOffset(float translation) {
        this.setTranslationY(translation);
    }

    private void setContainerHeight(int currHeight) {
        this.getLayoutParams().height = currHeight;
    }

    private void setPadding(int currentPadding) {
        this.setPadding(currentPadding, 0, 0, 0);
    }

    private void setTextSize(float currentTextSize) {
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentTextSize);
    }

    private void setAvatarSize(int currentImageSize) {
        avatarView.getLayoutParams().height = currentImageSize;
        avatarView.getLayoutParams().width = currentImageSize;
    }
}
