package com.epam.preproduction.helpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.epam.preproduction.components.NavigationLine;
import com.epam.preproduction.entities.BreadMaker;
import com.epam.preproduction.entities.Item;
import com.epam.preproduction.pages.CataloguePage;
import com.epam.preproduction.pages.PricePage;

public class PriceFilterTestHelper{

	private static CataloguePage cataloguePage;
	private static PricePage pricePage;

	public void setCataloguePage(CataloguePage cataloguePage, PricePage pricePage) {
		PriceFilterTestHelper.cataloguePage = cataloguePage;
		PriceFilterTestHelper.pricePage = pricePage;
	}

	public void verifyPriceFilter(double minPrice, double maxPrice) {

		List<Item> allMachines = new ArrayList<Item>();

		while (true) {

			allMachines.addAll(grabItems());

			System.out.println("allMachines.size: " + allMachines.size());
			if (hasNext()) {
				next();
			} else {
				break;
			}
		}

		System.out.println(allMachines.size());
		System.out.println(allMachines);

		pricePage.clickAtPriceFilters((int) maxPrice, (int) minPrice);

		List<Item> allFitredMachines = new ArrayList<Item>();
		while (true) {

			allFitredMachines.addAll(grabItems());

			System.out.println("allFitredMachines.size: "
					+ allFitredMachines.size());
			if (hasNext()) {
				next();
			} else {
				break;
			}
		}

		System.out.println(allFitredMachines.size());
		System.out.println(allFitredMachines);

		for (Item machine : allFitredMachines) {

			if (machine.getPrice() > maxPrice || machine.getPrice() < minPrice) {
				Assert.fail();
			}
		}

		String totalFilteredItems = cataloguePage.getFilterBlock()
				.getFilteredItemsCount().getText();
		System.out.println(totalFilteredItems);

		for (Item machine : allMachines) {
			if (machine.getPrice() <= maxPrice
					&& machine.getPrice() >= minPrice) {

				Assert.assertTrue(allFitredMachines.size() == Integer.parseInt(totalFilteredItems));
			}
		}
	}

	public List<Item> grabItems() {

		List<Item> result = new ArrayList<Item>();

		List<WebElement> items = cataloguePage.getMainBlock().getDivClassItem();
		System.out.println(items);
		for (WebElement item : items) {
			BreadMaker breadMaker = new BreadMaker();
			String name = cataloguePage.getMainBlock().getProductNames().getText();
			int price = extratNumbers(cataloguePage.getMainBlock().getPriceStrong().getText());
			String description = cataloguePage.getMainBlock().getDescription().getText();
			breadMaker.setName(name);
			breadMaker.setPrice(price);
			breadMaker.setDescription(description);
			result.add(breadMaker);
		}
		return result;
	}

	private static Integer extratNumbers(String price) {
		Integer pr = Integer.valueOf(price.substring(0, price.length() - 4)
				.replace(" ", ""));
		return pr;
	}

	protected boolean hasNext() {
		WebElement element = cataloguePage.getMainBlock().getNextPage();
		if (!CollectionUtils.isEmpty(element.findElements(By
				.className(NavigationLine.ACTIVE_LINK)))) {
			return true;
		}
		return false;
	}

	protected void next() {

		WebElement element = cataloguePage.getMainBlock().getNextPage();
		if (!CollectionUtils.isEmpty(element.findElements(By
				.className(NavigationLine.ACTIVE_LINK)))) {
			element.findElement(By.className(NavigationLine.ACTIVE_LINK)).click();
		}
	}

}