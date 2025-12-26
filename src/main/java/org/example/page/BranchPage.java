package org.example.page;

public abstract class BranchPage extends Page{
    public BranchPage(Navigator navigator) {
        super(navigator);
        menuMap.put(0, new MenuItem("Назад", navigator::getBack));
    }
}
