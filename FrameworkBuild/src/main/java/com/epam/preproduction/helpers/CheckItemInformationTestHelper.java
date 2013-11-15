package com.epam.preproduction.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;

import com.epam.preproduction.pages.CataloguePage;
import com.epam.preproduction.pages.ItemPage;
import com.epam.preproduction.pages.PricePage;

public class CheckItemInformationTestHelper {

	private static CataloguePage cataloguePage;
	private static PricePage pricePage;
	private static ItemPage itemPage;

	public static ItemPage getItemPage() {
		return itemPage;
	}

	public static void setItemPage(ItemPage itemPage) {
		CheckItemInformationTestHelper.itemPage = itemPage;
	}

	public static CataloguePage getCataloguePage() {
		return cataloguePage;
	}

	public static void setCataloguePage(CataloguePage cataloguePage) {
		CheckItemInformationTestHelper.cataloguePage = cataloguePage;
	}

	public static PricePage getPricePage() {
		return pricePage;
	}

	public static void setPricePage(PricePage pricePage) {
		CheckItemInformationTestHelper.pricePage = pricePage;
	}

	public static void setPages(CataloguePage cataloguePage,
			PricePage pricePage, ItemPage itemPage) {
		CheckItemInformationTestHelper.cataloguePage = cataloguePage;
		CheckItemInformationTestHelper.pricePage = pricePage;
		CheckItemInformationTestHelper.itemPage = itemPage;

	}

	public void verifyItemLinksAreEqual() {
		List<String> data = new ArrayList<String>();
		List<String> pricesLinks = new ArrayList<String>();
		List<String> namesList = new ArrayList<String>();
		List<String> catalogueLinks = new ArrayList<String>();
		List<String> pricePageLinks = new ArrayList<String>();
		gerUrls(data, pricesLinks);
		goToPricePage(namesList);
		Assert.assertEquals(catalogueLinks, pricePageLinks,
				"Some links are shown in search results by mistake! ");
		System.out.println();
	}

	public void gerUrls(List<String> catalogueLinks, List<String> pricePageLinks) {

		List<String> urlList = new ArrayList<String>();
		List<String> namesList = new ArrayList<String>();

		for (int i = 1; i < 6; i++) {
			List<WebElement> names = cataloguePage
					.getDriver()
					.findElements(
							By.xpath(cataloguePage.getCompareBlock().DIV_CLASS_ITEM_PART_1
									+ i
									+ cataloguePage.getCompareBlock().DIV_CLASS_ITEM_PART_2));
			for (WebElement webElement : names) {
				String hrefs = webElement.getAttribute(cataloguePage.getCompareBlock().HREF);
				String itemNames = webElement.getText();
				namesList.add(itemNames);
				catalogueLinks.add(hrefs);
			}
			cataloguePage
					.getDriver()
					.findElement(
							By.xpath(cataloguePage.getCompareBlock().DIV_CLASS_ITEM_PART_1
									+ i
									+ cataloguePage.getCompareBlock().DIV_CLASS_ITEM_PART_2))
					.click();
			urlList.add(i - 1, itemPage.getDriver().getCurrentUrl());
			cataloguePage.goBack();
			cataloguePage.refreshLocators();
			Assert.assertNotEquals(catalogueLinks, pricePageLinks, "Some links are shown in search results by mistake! ");

		}

		System.out.println(namesList);
		System.out.println(catalogueLinks);
		System.out.println(urlList);
		System.out.println(goToPricePage(namesList));

	}

	public Set<String> goToPricePage(List<String> namesList) {

		Set<String> pricePageLinks = new HashSet<String>();
		cataloguePage.getCompareBlock().getPricePageLink().click();
		for (int j = 0; j < namesList.size(); j++) {
			// pricePage.getDriver().findElement(By.id(cataloguePage.getCompareBlock().getSearchField())).sendKeys(namesList.get(j));
			cataloguePage.getCompareBlock().getEditField()
					.sendKeys(namesList.get(j));
			cataloguePage.getCompareBlock().getSearchField().click();

			// List<WebElement> linkToDescription =
			// pricePage.getDriver().findElements(By.xpath("//td[@class='n']/a[1]"));
			List<WebElement> linkToDescription = cataloguePage
					.getCompareBlock().getTdPricePage();

			for (WebElement webElement : linkToDescription) {
				String hrefs = webElement.getAttribute(cataloguePage.getCompareBlock().HREF);
				pricePageLinks.add(hrefs);

			}
			// pricePage.getDriver().findElement(By.id("edit-name-1")).clear();
			cataloguePage.getCompareBlock().getEditField().clear();

		}
		return pricePageLinks;
	}

}
