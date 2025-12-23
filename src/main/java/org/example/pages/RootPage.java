package org.example.pages;

public abstract class RootPage extends Page{
    public RootPage(Navigator navigator) {
        super(navigator);
    }

    @Override
    public void onEnter(){
        navigator.clearHistory();
    }
}
