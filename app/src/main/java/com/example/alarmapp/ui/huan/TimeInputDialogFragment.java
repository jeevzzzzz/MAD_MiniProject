package com.example.alarmapp.ui.huan;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.alarmapp.R;
import com.example.alarmapp.adapter.CountdownAdapter;


public class TimeInputDialogFragment extends DialogFragment {

    private EditText editTextHour;
    private EditText editTextMinute;
    private EditText editTextSecond;
    private EditText editTextTimeName;
    private Button buttonExit;
    private Button buttonAdd;

    CountdownAdapter recycleViewPresetTimeAdapter;


    public TimeInputDialogFragment(CountdownAdapter recycleViewPresetTimeAdapter){
        this.recycleViewPresetTimeAdapter = recycleViewPresetTimeAdapter;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_time_input_dialog, container, false);

        //Get id
        editTextHour = view.findViewById(R.id.edit_text_hour);
        editTextMinute = view.findViewById(R.id.edit_text_minute);
        editTextSecond = view.findViewById(R.id.edit_text_second);
        editTextTimeName = view.findViewById(R.id.edit_text_time_name);
        buttonExit = view.findViewById(R.id.button_exit);
        buttonAdd = view.findViewById(R.id.button_add);


        //get data from picker activity
        Bundle bundle = getArguments();
        if (bundle != null) {
            String hour = bundle.getString("hour");
            editTextHour.setText(String.format("%02d", Integer.parseInt(hour)));

            String minute = bundle.getString("minute");
            editTextMinute.setText(String.format("%02d", Integer.parseInt(minute)));

            String second = bundle.getString("second");
            editTextSecond.setText(String.format("%02d", Integer.parseInt(second)));
        }


        //Tạo sự kiện chỉnh sửa text cho giờ
        editTextHour.setSelectAllOnFocus(true);
        editTextHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần thực hiện hành động trước khi văn bản thay đổi
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần thực hiện hành động trong quá trình văn bản thay đổi
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newText = editable.toString();
                if (newText.length() > 2) {
                    // Nếu độ dài lớn hơn 2, chỉ lấy 2 ký tự đầu tiên
                    newText = newText.substring(0, 2);
                    editTextHour.setText(newText);
                    // Di chuyển con trỏ văn bản về cuối
                    editTextHour.setSelection(newText.length());
                }
            }
        });
        editTextHour.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Kiểm tra xem EditText đã mất focus
                if (!hasFocus) {
                    // Xử lý chỉnh sửa nội dung khi người dùng chuyển sang vùng khác
                    String newText = editTextHour.getText().toString();
                    int num = newText.equals("") ? 0 : Integer.parseInt(newText);
                    // Thực hiện việc chỉnh sửa hoặc xử lý dữ liệu ở đây
                    editTextHour.setText(String.format("%02d", num));

                }
            }
        });


        //Tạo sự kiện chỉnh sửa text cho phút
        editTextMinute.setSelectAllOnFocus(true);
        editTextMinute.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Kiểm tra xem EditText đã mất focus
                if (!hasFocus) {
                    // Xử lý chỉnh sửa nội dung khi người dùng chuyển sang vùng khác
                    String newText = editTextMinute.getText().toString();
                    // Thực hiện việc chỉnh sửa hoặc xử lý dữ liệu ở đây



                    int num = newText.equals("") ? 0 : Integer.parseInt(newText);
                    if(num > 59) {
                        num = 59;
                    }

                    editTextMinute.setText(String.format("%02d", num));

                }
            }
        });

        editTextMinute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần thực hiện hành động trước khi văn bản thay đổi
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần thực hiện hành động trong quá trình văn bản thay đổi
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newText = editable.toString();
                if (newText.length() > 2) {
                    // Nếu độ dài lớn hơn 2, chỉ lấy 2 ký tự đầu tiên
                    newText = newText.substring(0, 2);
                    editTextMinute.setText(newText);
                    // Di chuyển con trỏ văn bản về cuối
                    editTextMinute.setSelection(newText.length());
                }
            }
        });


        //Tạo sự kiện chỉnh sửa text cho giây
        editTextSecond.setSelectAllOnFocus(true);
        editTextSecond.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Kiểm tra xem EditText đã mất focus
                if (!hasFocus) {
                    // Xử lý chỉnh sửa nội dung khi người dùng chuyển sang vùng khác
                    String newText = editTextSecond.getText().toString();

                    int num = newText.equals("") ? 0 : Integer.parseInt(newText);
                    if(num > 59) {
                        num = 59;
                    }
                    editTextSecond.setText(String.format("%02d", num));

                }
            }
        });
        editTextSecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần thực hiện hành động trước khi văn bản thay đổi
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần thực hiện hành động trong quá trình văn bản thay đổi
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newText = editable.toString();
                if (newText.length() > 2) {
                    // Nếu độ dài lớn hơn 2, chỉ lấy 2 ký tự đầu tiên
                    newText = newText.substring(0, 2);
                    editTextSecond.setText(newText);
                    // Di chuyển con trỏ văn bản về cuối
                    editTextSecond.setSelection(newText.length());
                }
            }
        });


        // Cài sự kiện cho button exit
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        // Thêm preset time
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextTimeName.requestFocus();
                saveTimeInput();
                dismiss();
            }
        });

        return view;
    }


    // lưu preset time
    public void saveTimeInput(){
        String hour = String.valueOf(editTextHour.getText());
        String minute = String.valueOf(editTextMinute.getText());
        String second = String.valueOf(editTextSecond.getText());
        String description = String.valueOf(editTextTimeName.getText());

        String result = hour +":"+ minute +":"+ second + "|" + description;
        if(result != "00:00:00|") {

            int newPosition = recycleViewPresetTimeAdapter
                    .getLocalDataSet().size() - 1; // Vị trí của item mới
            recycleViewPresetTimeAdapter.addItem(newPosition / 2, result);
            recycleViewPresetTimeAdapter.onItemClick(newPosition / 2);
            //recycleViewPresetTimeAdapter.notifyItemInserted(newPosition + 1);
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext(), R.style.DialogStyle);
        dialog.setContentView(R.layout.fragment_time_input_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        return dialog;
    }
}