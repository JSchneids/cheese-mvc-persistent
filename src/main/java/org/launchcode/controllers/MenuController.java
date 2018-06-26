package org.launchcode.controllers;

import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private MenuDao menuDao;

    @RequestMapping(value = "")
    public String index(Model model){
        model.addAttribute("title", "Menus");
        model.addAttribute("menus", menuDao.findAll());

        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddmenuForm(Model model) {
        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());

        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddMenuForm(@ModelAttribute @Valid Menu menu,
                                     Errors errors,
                                     Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }

    @RequestMapping(value="view/{menuId}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int menuId) {

        model.addAttribute("menu", menuDao.findOne(menuId));

        return "menu/view";
    }

    @RequestMapping(value = "add-item/{menuId}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int menuId){

        Menu menu = menuDao.findOne(menuId);

        AddMenuItemForm form = new AddMenuItemForm(menu, cheeseDao.findAll());

        model.addAttribute("form", form);
        model.addAttribute("title", "Add item to Menu");

        return "menu/add-item";

    }

    @RequestMapping(value="add-item/{menuId}", method = RequestMethod.POST)
    public String processAddItem(Model model, AddMenuItemForm addMenuItemForm, Errors errors){

        if (errors.hasErrors()){
            return "menu/add-item";
        }

        Menu menu = menuDao.findOne(addMenuItemForm.getMenuId());

        menu.addItem(cheeseDao.findOne(addMenuItemForm.getCheeseId()));

        menuDao.save(menu);

        return "redirect:../view/" + menu.getId();

    }
}