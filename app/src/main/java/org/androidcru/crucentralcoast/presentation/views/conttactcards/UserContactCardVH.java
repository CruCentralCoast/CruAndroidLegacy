package org.androidcru.crucentralcoast.presentation.views.conttactcards;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruUser;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;
import org.androidcru.crucentralcoast.presentation.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserContactCardVH extends RecyclerView.ViewHolder
{
    @BindView(R.id.icon) ImageView icon;
    @BindView(R.id.title) TextView passengerName;
    @BindView(R.id.subtitle) TextView passengerPhone;
    @BindView(R.id.action2) ImageButton addToContacts;
    private ColorGenerator generator = ColorGenerator.MATERIAL;

    private Context context;
    private CruUser model;

    public UserContactCardVH(View itemView)
    {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = itemView.getContext();

        addToContacts.setImageDrawable(DrawableUtil.getDrawable(itemView.getContext(), R.drawable.contact_mail));
    }


    public void bind(CruUser user)
    {
        this.model = user;
        bindUI();
    }

    private void bindUI()
    {
        passengerName.setText(model.name.toString());
        passengerPhone.setText(model.phone);

        int color = generator.getColor(model.name);


        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .useFont(ViewUtil.getFont(addToContacts.getContext(), AppConstants.FREIG_SAN_PRO_LIGHT))
                .endConfig()
                .buildRound(model.name.initialis(), color);

        icon.setImageDrawable(drawable);
    }

    @OnClick(R.id.action2)
    protected void onAddToContact()
    {
        context.startActivity(ViewUtil.insertOrEditContact(model.name.toString(), model.phone));
    }
}
