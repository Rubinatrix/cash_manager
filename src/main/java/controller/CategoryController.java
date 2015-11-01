package controller;

import domain.Category;
import domain.User;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import service.CategoryService;
import utils.CashManagerException;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    protected static Logger logger = Logger.getLogger("org/controller");

    @Resource(name="categoryService")
    private CategoryService categoryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getCategories(Model model) {
        logger.debug("Received request to show category list");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Category> categories = categoryService.getAllByUser(user);
        model.addAttribute("categories", categories);
        model.addAttribute("username", user.getUsername());
        return "category-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String getAdd(Model model) {
        logger.debug("Received request to show add category page");
        Category category = new Category();
        model.addAttribute("categoryAttribute", category);
        model.addAttribute("type", "add");
        return "category";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String postAdd(@ModelAttribute("categoryAttribute") Category category) {
        logger.debug("Received request to add new category");
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        categoryService.add(category, user);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String getDelete(@RequestParam("id") Long categoryId, Model model) {
        logger.debug("Received request to delete category");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Category> categories = categoryService.getAllByUser(user);
        model.addAttribute("categories", categories);
        model.addAttribute("username", user.getUsername());

        try {
            categoryService.delete(categoryId);
        } catch (CashManagerException e) {
            model.addAttribute("errorDescription", e.getMessage());
        }

        return "category-list";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String getEdit(@RequestParam("id") Long categoryId, Model model) {
        logger.debug("Received request to show edit category page");
        Category existingCategory = categoryService.get(categoryId);
        model.addAttribute("categoryAttribute", existingCategory);
        model.addAttribute("type", "edit");
        return "category";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String postEdit(@RequestParam("id") Long categoryId, @ModelAttribute("currencyAttribute") Category category) {
        logger.debug("Received request to add new category");
        category.setId(categoryId);
        categoryService.edit(category);
        return "redirect:list";
    }

}