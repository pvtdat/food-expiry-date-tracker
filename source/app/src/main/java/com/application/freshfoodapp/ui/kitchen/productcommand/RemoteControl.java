package com.application.freshfoodapp.ui.kitchen.productcommand;

import com.application.freshfoodapp.adapter.KitchenTypeAdapter;
import com.application.freshfoodapp.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Stack;

public class RemoteControl {
    Stack<Product> undoStack;
    public RemoteControl() {
        undoStack = new Stack<>();
    }
    public void deleteBtnWasPushed(Product product,
                                   List<Product> data,
                                   KitchenTypeAdapter adapter,
                                   int deletePosition) {
        CommandBase commandBases = new ProductCommand(product);
        commandBases.deleting(product, data, adapter, deletePosition);
        undoStack.push(product);
    }
    public Product addBtnWasPushed(Product product) {
        CommandBase commandBases = new ProductCommand(product);
        commandBases.adding();
        return product;
    }
    public Product undoBtnWasPushed() {
        Product product = undoStack.pop();
        if (product != null) {
            addBtnWasPushed(product);
            undoStack.remove(product);
        }
        return product;
    }
}