package com.CSCI.a3130_group_6.HelperClases;
import com.CSCI.a3130_group_6.Listings.Listing;

import java.util.*;

/**
 * Helper class to sort listings by date and location
 *
 * @author  Pulkit, Han
 */
public class SortHelper {

    public SortHelper(){
    }

    /**
     * Function: This method returns sorted Listing arraylist in ascending order of date
     * Parameters: ArrayList<Listing>
     * @return: ArrayList<Listing>
     * Reference: https://www.geeksforgeeks.org/sort-an-array-of-dates-in-ascending-order-using-custom-comparator/
     */
    public List<Listing> sortDatesDescending(List<Listing> arr)
    {

        // Sort the dates using library
        // sort function with custom Comparator
        Collections.sort(arr,new Comparator<Listing>()
        {
            public int compare(Listing l1, Listing l2)
            {
                String date1 = l1.getDate();
                String date2 = l2.getDate();
                String day1 = date1.substring(0, 2);
                String month1 = date1.substring(3, 5);
                String year1 = date1.substring(6);

                String day2 = date2.substring(0, 2);
                String month2 = date2.substring(3, 5);
                String year2 = date2.substring(6);

                // Condition to check the year
                if (year2.compareTo(year1) > 0)
                    return 1;
                else if (year2.compareTo(year1) < 0)
                    return -1;

                    // Condition to check the month
                else if (month2.compareTo(month1) > 0)
                    return 1;
                else if (month2.compareTo(month1) < 0)
                    return -1;

                    // Condition to check the day
                else if (day2.compareTo(day1) > 0)
                    return 1;
                else
                    return -1;
            }
        });

        return arr;
    }

    /**
     * Function: This method is used to sort the hashMap with Listing as keys and distance as values
     *           by values
     * Parameters: HashMap<Listing, Double>
     * @return: HashMap<Listing, Double>
     * Reference: https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
     */
    public HashMap<Listing, Double> sortByValue(HashMap<Listing, Double> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<Listing, Double> > list =
                new LinkedList<Map.Entry<Listing, Double> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Listing, Double> >() {
            public int compare(Map.Entry<Listing, Double> o1,
                               Map.Entry<Listing, Double> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Listing, Double> temp = new LinkedHashMap<Listing, Double>();
        for (Map.Entry<Listing, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public ArrayList<Integer> getSortedPositions(ArrayList<Listing> listings, List<Listing> locationListing) {
        ArrayList<Integer> sortPositions = new ArrayList<>();
        for (int i = 0; i < listings.size(); i++) {
            for (int j = 0; j <locationListing.size(); j++) {
                if (listings.get(i) == locationListing.get(j)) {
                    sortPositions.add(j);
                    break;
                } else if (j == locationListing.size() - 1) {
                    sortPositions.add(-1);
                }
            }
        }
        return sortPositions;
    }

    public ArrayList<String> sortArrayListByPosition(ArrayList<String> list, ArrayList<Integer> sortPositions){
        ArrayList<String> sortedList = new ArrayList<>();
        int fill = Collections.max(sortPositions);
        int k = 0;
        while (k <= fill){
            sortedList.add("Test");
            k++;
        }
        for(int i = 0; i < sortPositions.size(); i++) {
            // [-1, 1, 0]
            if (sortPositions.get(i) != -1) {
                sortedList.set(sortPositions.get(i), list.get(i));
            }
        }
        return sortedList;
    }
}