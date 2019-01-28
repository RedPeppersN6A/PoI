package fr.ensisa.letaif;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<PoIModel> poIModelList;
    private View.OnLongClickListener longClickListener;
    private View.OnClickListener singleClickListener;


    public RecyclerViewAdapter(List<PoIModel> poIModelList, View.OnLongClickListener longClickListener,View.OnClickListener onClickListener){
        this.poIModelList = poIModelList;
        this.longClickListener = longClickListener;
        this.singleClickListener = onClickListener;
    }

    @Override
    @NonNull
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rvpoirow, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {
        PoIModel poIModel = poIModelList.get(position);
        holder.poINameView.setText(poIModel.getPoIName());
        holder.poICreatorNameView.setText("By "+poIModel.getPoICreatorName());
        holder.poIDateView.setText(poIModel.getPoICreationDate().toLocaleString().substring(0, 13));
        holder.poINoteView.setText(poIModel.getNote()+"/5");
        holder.poIDescriptionView.setText(poIModel.getDescription());
        holder.poIId.setText(String.valueOf(poIModel.id));
        holder.itemView.setTag(poIModel);
        holder.itemView.setOnClickListener(singleClickListener);
        holder.itemView.setOnLongClickListener(longClickListener);
    }

    @Override
    public int getItemCount() {
        return poIModelList.size();
    }

    public void addPoI(List<PoIModel> poIModelList) {
        this.poIModelList = poIModelList;
        notifyDataSetChanged();
    }
    public PoIModel getPoIModelAtPosition (int position) {
        return poIModelList.get(position);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView poINameView;
        private TextView poICreatorNameView;
        private TextView poIDateView;
        private TextView poIDescriptionView;
        private TextView poINoteView;
        private TextView poIId;

        RecyclerViewHolder(View view) {
            super(view);
            poINameView = view.findViewById(R.id.tvPoIName);
            poICreatorNameView = (TextView) view.findViewById(R.id.tvPoICreatorName);
            poIDescriptionView = (TextView) view.findViewById(R.id.tvPoIDescription);
            poINoteView = (TextView) view.findViewById(R.id.tvPoIRate);
            poIDateView = (TextView) view.findViewById(R.id.tvPoIDate);
            poIId = (TextView) view.findViewById(R.id.tvPoIId);
        }
    }
}
