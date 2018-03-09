package com.crucentralcoast.app.presentation.views.updategroupsinformation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.Manifest;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.CommunityGroup;
import com.crucentralcoast.app.data.models.CruUser;
import com.crucentralcoast.app.data.models.MinistryTeam;
import com.crucentralcoast.app.data.providers.CommunityGroupProvider;
import com.crucentralcoast.app.data.providers.MinistryTeamProvider;
import com.crucentralcoast.app.data.providers.UpdateGroupsInformationProvider;
import com.crucentralcoast.app.presentation.util.ViewUtil;
import com.crucentralcoast.app.presentation.views.base.BaseSupportFragment;
import com.squareup.picasso.Picasso;

import org.threeten.bp.DayOfWeek;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Dylan on 2/15/18.
 */

//USE S3 CONTAINER PHOTOS

public class UpdateMinistryTeamFragment extends BaseSupportFragment {

    @BindView(R.id.update_ministry_team_button)
    protected Button updateButton;
    @BindView(R.id.update_ministy_team_cancel_button)
    protected Button cancelButton;
    @BindView(R.id.update_team_title_field)
    protected TextView groupTitle;
    @BindView(R.id.update_name_field)
    protected EditText updateTeamNameField;
    @BindView(R.id.update_description_field)
    protected EditText updateTeamDescriptionField;
    @BindView(R.id.ministry_team_image)
    protected ImageView teamImageField;


    private static String ministryTeamID;
    private static MinistryTeam ministryTeam = null;
    public static Observer<MinistryTeam> ministryTeamObserver;

    private String teamName;
    private String teamDescription;
    private String teamImageLink;
    private String teamParentMinistryID;
    private List<CruUser> teamLeaders;

    private static final int UPLOAD_FROM_GALLERY = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 4;


    private CompositeSubscription compSub;

    public UpdateMinistryTeamFragment newInstance() {
        return new UpdateMinistryTeamFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compSub = new CompositeSubscription();
        setupMinistryTeamObserver();
        if (!getArguments().isEmpty())
            ministryTeamID = getArguments().getString("groupID");
        else {
            Timber.e("Missing information to update ministry team");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_update_ministry_team, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("in view created");
        MinistryTeamProvider.getMinistryTeam(UpdateMinistryTeamFragment.this, ministryTeamObserver, ministryTeamID);
        ViewUtil.setFont(cancelButton, AppConstants.FREIG_SAN_PRO_LIGHT);
        ViewUtil.setFont(updateButton, AppConstants.FREIG_SAN_PRO_LIGHT);

    }


    public void setupMinistryTeamObserver() {
        ministryTeamObserver = new Observer<MinistryTeam>() {
            @Override
            public void onCompleted() {
                Timber.d("hello on complete");
                setFieldText();
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "error bruh:" + e.toString());
            }

            @Override
            public void onNext(MinistryTeam retrievedMinistryTeam) {
                ministryTeam = retrievedMinistryTeam;
            }
        };

    }

    public void setFieldText() {
        groupTitle.setText("Updating " + ministryTeam.name);
        teamName = ministryTeam.name;
        teamDescription = ministryTeam.description;
        teamImageLink = ministryTeam.teamImage;
        teamLeaders = ministryTeam.ministryTeamLeaders;
        teamParentMinistryID = ministryTeam.parentMinistryId;

        updateTeamNameField.setText(teamName);
        updateTeamDescriptionField.setText(teamDescription);

        Picasso.with(this.getContext()).load(teamImageLink).fit().centerCrop().into(teamImageField);

        teamImageField.isClickable();
        teamImageField.setOnClickListener(view -> getNewImage());

    }

    public void getNewImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Timber.d("in get new image!!!");
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Gallery", "Camera"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selectedOption) {
                        switch (selectedOption) {
                            case 0:
                                getImageFromGallery();
                                break;
                            case 1:
                                getImageFromCamera();
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.show();

    }

    private void getImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Intent chooser = Intent.createChooser(intent, "Choose a Picture");
        startActivityForResult(chooser, UPLOAD_FROM_GALLERY);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        if (reqCode == UPLOAD_FROM_GALLERY && resultCode == RESULT_OK) {

            try {
                final Uri uri = data.getData();
                final InputStream imageInputStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap selectedImage = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(imageInputStream), 400, 400, false);
                teamImageField.setImageBitmap(selectedImage);
            }
            catch (Exception e) {
            }

        }
        if (reqCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            teamImageField.setImageBitmap(photo);
        }
    }

    private void getImageFromCamera() {
        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(imageIntent, REQUEST_IMAGE_CAPTURE);
    }

    @OnClick(R.id.update_ministy_team_cancel_button)
    public void onCLickUpdateCommunityGroupCancelButton() {
        createAlertDialog(getString(R.string.create_account_cancel), getString(R.string.create_account_cancel_message), getString(R.string.alert_dialog_yes), getString(R.string.alert_dialog_no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();

                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }
        );

    }

    @OnClick(R.id.update_ministry_team_button)
    public void onClickUpdateMinistryTeamButton() {

        String mtTeamName = updateTeamNameField.getText().toString();
        String mtTeamDescription = updateTeamDescriptionField.getText().toString();


        MinistryTeam updateMinistryTeam = new MinistryTeam(ministryTeamID, mtTeamDescription, mtTeamName, teamParentMinistryID, teamLeaders);

        compSub.add(
              UpdateGroupsInformationProvider.updateMinistryTeam(ministryTeamID,  updateMinistryTeam)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                          response -> {
                              getActivity().finish();
                              Toast.makeText(getActivity(), getString(R.string.update_ministry_team_success), Toast.LENGTH_LONG).show();
                          },
                          error -> {
                              Timber.e(error);
                          }
                    )
        );
    }

    public void createAlertDialog(String title, String message, String postiveText, String negativeText, DialogInterface.OnClickListener positveDialogListener, DialogInterface.OnClickListener negativeDialogListener) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(message);

        if (negativeText != null) {
            alertBuilder.setNegativeButton(negativeText,
                    negativeDialogListener);
        }
        alertBuilder.setPositiveButton(postiveText,
                positveDialogListener);

        alertBuilder.show();
    }


}
