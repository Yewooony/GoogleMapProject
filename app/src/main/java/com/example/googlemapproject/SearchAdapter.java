package com.example.googlemapproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * RecyclerView에서 검색 결과를 처리하기 위한 어댑터 클래스
 * 이 어댑터는 SearchItem 객체들을 리스트 형식으로 표시하는 것을 관리함
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    // 검색 결과 항목들을 저장하는 리스트
    private List<SearchItem> items;
    // 항목 클릭 이벤트를 처리하기 위한 인터페이스 참조
    private OnItemClickListener listener;

    /**
     * 항목 클릭 이벤트를 처리하기 위한 인터페이스 정의
     * 구현하는 클래스는 반드시 onItemClick 동작을 정의해야 함
     */
    public interface OnItemClickListener {
        void onItemClick(SearchItem item);
    }

    /**
     * SearchAdapter의 생성자
     * @param items 표시할 검색 항목 리스트
     * @param listener 항목 클릭 이벤트를 위한 콜백
     */
    public SearchAdapter(List<SearchItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    /**
     * 새로운 ViewHolder 인스턴스를 생성함
     * RecyclerView가 항목을 표시하기 위한 새로운 ViewHolder가 필요할 때 호출됨
     * @param parent 새로운 View가 추가될 ViewGroup
     * @param viewType 새로운 View의 유형
     * @return item_search 레이아웃을 담고 있는 새로운 SearchViewHolder
     */
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search, parent, false);
        return new SearchViewHolder(view);
    }

    /**
     * 주어진 위치의 항목을 표시하기 위해 ViewHolder의 내용을 업데이트함
     * @param holder 업데이트할 ViewHolder
     * @param position 데이터 셋에서의 항목 위치
     */
    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    /**
     * 데이터 셋의 전체 항목 수를 반환
     * @return items 리스트의 크기
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * 검색 항목을 위한 ViewHolder 클래스
     * 항목 레이아웃 내의 뷰들에 대한 참조를 보유함
     */
    class SearchViewHolder extends RecyclerView.ViewHolder {
        // 검색 결과의 이름을 표시하기 위한 TextView
        private TextView nameText;
        // 검색 결과의 주소를 표시하기 위한 TextView
        private TextView addressText;

        /**
         * SearchViewHolder의 생성자
         * 뷰 참조를 초기화하고 클릭 리스너를 설정함
         * @param itemView 항목 레이아웃을 포함하는 뷰
         */
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            addressText = itemView.findViewById(R.id.addressText);

            // 항목 클릭 시 이벤트 처리를 위한 리스너 설정
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(items.get(position));
                }
            });
        }

        /**
         * ViewHolder에 데이터를 바인딩
         * @param item 표시할 검색 항목 데이터
         */
        public void bind(SearchItem item) {
            nameText.setText(item.getName());
            addressText.setText(item.getAddress());
        }
    }
}