package com.tool.rss.ui.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tool.rss.R;
import com.tool.rss.ui.model.TrashResultEntity;

public class HomeAdapter extends BaseQuickAdapter<TrashResultEntity.DataBean, BaseViewHolder> {

    public HomeAdapter() {
        super(R.layout.adapter_home);
    }

    @Override
    protected void convert(BaseViewHolder helper, TrashResultEntity.DataBean item) {
        helper.setText(R.id.adapter_home_txt, item.Name);
        TextView mTvStatu = helper.getView(R.id.adapter_home_statu);
        String garbageName = "";
        int gravageColor = R.color.tarch_g;
        switch (item.Kind) {
            case 1:
                garbageName = "可回收物";
                gravageColor = R.color.tarch_k;
                break;
                case 2:
                    garbageName = "有害垃圾";
                    gravageColor = R.color.tarch_y;
                    break;
                    case 3:
                        garbageName = "易腐垃圾";
                        gravageColor = R.color.tarch_s;
                        break;
                        case 4:
                            garbageName = "其他垃圾";
                            gravageColor = R.color.tarch_g;
                            break;
                            case 1008:
                                garbageName = item.msg;
                                gravageColor = R.color.tarch_null;
                                break;
                                default:
                                    garbageName = "不属于日常生活垃圾";
                                    gravageColor = R.color.tarch_null;
                                    break;
        }
        mTvStatu.setText(garbageName);
        mTvStatu.setTextColor(mContext.getResources().getColor(gravageColor));
    }
}
