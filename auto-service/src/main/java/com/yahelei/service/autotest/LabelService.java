package com.yahelei.service.autotest;

import com.yahelei.domain.autotest.LabelBean;

import java.util.List;
import java.util.Map;

public interface LabelService {

    List<LabelBean> getLabelList(String project_id, String description);

    void updatelabel(LabelBean labelBean);

    void insertlabel(LabelBean labelBean);

    String executebylabelid(String Labelid);
}