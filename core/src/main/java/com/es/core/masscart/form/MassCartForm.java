package com.es.core.masscart.form;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

public class MassCartForm {
    private List<MassCartItemForm> massCartItemFormList;

    public MassCartForm() {
        massCartItemFormList = new ArrayList<>();
    }

    public List<MassCartItemForm> getMassCartItemFormList() {
        return massCartItemFormList;
    }

    public void setMassCartItemFormList(List<MassCartItemForm> massCartItemFormList) {
        this.massCartItemFormList = massCartItemFormList;
    }
}
