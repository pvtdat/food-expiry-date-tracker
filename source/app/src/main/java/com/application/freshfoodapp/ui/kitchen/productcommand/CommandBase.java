package com.application.freshfoodapp.ui.kitchen.productcommand;

import com.application.freshfoodapp.adapter.KitchenTypeAdapter;
import com.application.freshfoodapp.model.Product;

import java.util.List;

public abstract class CommandBase {
    public CommandBase() {
    }

    abstract protected void executeDelete(Product product,
                                          List<Product> data,
                                          KitchenTypeAdapter adapter,
                                          int deletePosition);

    abstract protected void executeAdd();

    public void deleting(Product product,
                         List<Product> data,
                         KitchenTypeAdapter adapter,
                         int deletePosition) {
        executeDelete(product, data, adapter, deletePosition);
    }

    public void adding() {
        executeAdd();
    }
}