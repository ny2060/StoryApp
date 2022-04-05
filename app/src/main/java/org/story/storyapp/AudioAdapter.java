package org.story.storyapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.MyViewHolder> {

    //리사이클러뷰에 넣을 데이터 리스트
    ArrayList<Page> dataModels;
    ArrayList<Uri> dataUri;
    Context context;
    private int focusedItem = -1;
    private Boolean isFirst = false;
    // 리스너 객체 참조를 저장하는 변수
    private OnIconClickListener listener = null;
    private OnRecordClickListener listenerRecord = null;
    private OnMoveClickListener listenerMove = null;

    //생성자를 통하여 데이터 리스트 context를 받음
    public AudioAdapter(Context context, ArrayList<Page> dataModels) {
        this.dataModels = dataModels;
        this.context = context;
    }

    // 2. 리스너 객체를 전달하는 메서드와 전달된 객체를 저장할변수 추가
    public void setOnItemClickListener(OnIconClickListener listener) {
        this.listener = listener;
    }

    public void setOnRecordClickListener(OnRecordClickListener listener) {
        this.listenerRecord = listener;
    }

    public void setOnMoveClickListener(OnMoveClickListener listener) {
        this.listenerMove = listener;
    }

    @Override
    public int getItemCount() {
        //데이터 리스트의 크기를 전달해주어야 함
        return dataModels.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //자신이 만든 itemview를 inflate한 다음 뷰홀더 생성
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        //생선된 뷰홀더를 리턴하여 onBindViewHolder에 전달한다.
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //MyViewHolder myViewHolder = (MyViewHolder)holder;

        String uriName = dataModels.get(position).record;
        String url = dataModels.get(position).image;
        File file = new File(url);
        holder.storyImage.setImageBitmap(BitmapFactory.decodeFile(url));
        //Glide.with(holder.storyImage.getContext()).load(Uri.fromFile(file)).into(holder.storyImage);

        if (focusedItem != -1 && position == focusedItem) {
            holder.page.setBackgroundResource(R.drawable.bg_page_stroke);
            holder.pageMove.setVisibility(View.VISIBLE);
            holder.pagePlay.setVisibility(View.INVISIBLE);
            holder.page.setTag("select");
        } else {
            holder.page.setTag("none");
            holder.page.setBackgroundResource(R.drawable.bg_page_stroke_none);
            holder.pageMove.setVisibility(View.INVISIBLE);

            if (uriName.equals("")) {
                holder.pagePlay.setVisibility(View.INVISIBLE);
            } else {
                holder.pagePlay.setVisibility(View.VISIBLE);
            }

        }


    }

    // 1.커스텀 리스너 인터페이스 정의
    public interface OnIconClickListener {
        void onItemClick(View view, int position);
        //void onRecordClick(View view, int position);
    }


    public interface OnRecordClickListener {
        void onItemClick(View view, int position);
    }


    public interface OnMoveClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView storyImage;
        ConstraintLayout page;
        Button pageMove;
        ImageView pagePlay;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            storyImage = itemView.findViewById(R.id.iv_story);
            page = itemView.findViewById(R.id.page);
            pageMove = itemView.findViewById(R.id.btn_page_move);
            pagePlay = itemView.findViewById(R.id.iv_page_play);


            pagePlay.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //3. 아이템 클릭 이벤트 핸들러 메스드에서 리스너 객체 메서드 호출
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (listener != null) {
                            listener.onItemClick(view, pos);
                        }
                    }
                }
            });

            page.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //3. 아이템 클릭 이벤트 핸들러 메스드에서 리스너 객체 메서드 호출
                    listenerRecord.onItemClick(view, getAdapterPosition());
                    isFirst = true;
                    if (page.getTag().equals("none")) {
                        page.setBackgroundResource(R.drawable.bg_page_stroke);
                        page.setTag("select");
                        focusedItem = getAdapterPosition();
                        pageMove.setVisibility(View.VISIBLE);

                    } else {
                        page.setBackground(null);
                        page.setTag("none");
                        focusedItem = -1;
                        pageMove.setVisibility(View.INVISIBLE);
                    }
                    notifyDataSetChanged();
                }
            });
            pageMove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenerMove.onItemClick(view, getAdapterPosition());
                }
            });


        }
    }
}
