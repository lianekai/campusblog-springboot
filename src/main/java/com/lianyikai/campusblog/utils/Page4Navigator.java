package com.lianyikai.campusblog.utils;

import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

import java.util.List;

/*
 * 分页工具类
 * */
public class Page4Navigator<T> {
    Page<T> pageFromJPA;
    int navigatePages;
     
    int totalPages;
 
    int number;
     
    long totalElements;
     
    int size;

    int current; // 当前页
 
    int numberOfElements;
 
    List<T> content;
 
    boolean isHasContent;
 
    boolean first;
 
    boolean last;
     
    boolean isHasNext;
 
    boolean isHasPrevious;
     
    int[] navigatepageNums;
    int[] pageLimitNums;
     
    public Page4Navigator() {
        //这个空的分页是为了 Redis 从 json格式转换为 Page4Navigator 对象而专门提供的
    }

    public Page4Navigator(int current, List<T> beans, long total, int size, int navigatePages) {
        this.navigatePages = navigatePages;
        this.current = current;
        this.totalPages = (int) Math.ceil((double) total/size);
        this.number = current - 1;
        this.totalElements = total;
        this.size = size;
        this.numberOfElements = beans.size();
        this.content = beans;
        this.isHasContent = !CollectionUtils.isEmpty(beans);
        this.first = current == 1;
        this.last = current == this.totalPages;
        this.isHasNext = current != this.totalPages;
        this.isHasPrevious = current != 1;

        calcNavigatepageNums();
        calcLimitpageNums();
    }

    public Page4Navigator(int current, Page<T> pageFromJPA, int navigatePages) {
        this.pageFromJPA = pageFromJPA;
        this.navigatePages = navigatePages;
        this.current = current;
         
        totalPages = pageFromJPA.getTotalPages();
         
        number  = pageFromJPA.getNumber() ;
         
        totalElements = pageFromJPA.getTotalElements();
         
        size = pageFromJPA.getSize();
         
        numberOfElements = pageFromJPA.getNumberOfElements();
         
        content = pageFromJPA.getContent();
         
        isHasContent = pageFromJPA.hasContent();
                 
        first = pageFromJPA.isFirst();
         
        last = pageFromJPA.isLast();
         
        isHasNext = pageFromJPA.hasNext();
         
        isHasPrevious  = pageFromJPA.hasPrevious();
         
        calcNavigatepageNums();
        calcLimitpageNums();
    }
 
    private void calcNavigatepageNums() {
        int navigatepageNums[];
        int totalPages = getTotalPages();
        int num = getNumber();
        //当总页数小于或等于导航页码数时
        if (totalPages <= navigatePages) {
            navigatepageNums = new int[totalPages];
            for (int i = 0; i < totalPages; i++) {
                navigatepageNums[i] = i + 1;
            }
        } else { //当总页数大于导航页码数时
            navigatepageNums = new int[navigatePages];
            int startNum = num - navigatePages / 2;
            int endNum = num + navigatePages / 2;
 
            if (startNum < 1) {
                startNum = 1;
                //(最前navigatePages页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            } else if (endNum > totalPages) {
                endNum = totalPages;
                //最后navigatePages页
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatepageNums[i] = endNum--;
                }
            } else {
                //所有中间页
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            }
        }  
        this.navigatepageNums = navigatepageNums;
    }

    private void calcLimitpageNums() {
        int pageLimitNums[];
        int totalPages = getTotalPages();
        if (totalPages > 2) {
            // 当前页码为1 || 2时
            if (current == 1 || current == 2) {
                pageLimitNums = new int[3];
                for (int i = 0; i < 3; i++) {
                    pageLimitNums[i] = i + 1;
                }
            } else if (current == totalPages || current == totalPages - 1) { // 当前页码为最后一页 || 最后一页前一页时
                pageLimitNums = new int[3];
                for (int i = 0; i < 3; i++) {
                    pageLimitNums[i] = totalPages - 2 + i;
                }
            } else {
                pageLimitNums = new int[3];
                pageLimitNums[0] = current - 1;
                pageLimitNums[1] = current;
                pageLimitNums[2] = current + 1;
            }
        } else {
            pageLimitNums = new int[totalPages];
            for (int i = 0; i < totalPages; i++) {
                pageLimitNums[i] = i + 1;
            }
        }
        this.pageLimitNums = pageLimitNums;
    }
 
    public int getNavigatePages() {
        return navigatePages;
    }
 
    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }
 
    public int getTotalPages() {
        return totalPages;
    }
 
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
 
    public int getNumber() {
        return number;
    }
 
    public void setNumber(int number) {
        this.number = number;
    }
 
    public long getTotalElements() {
        return totalElements;
    }
 
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
 
    public int getSize() {
        return size;
    }
 
    public void setSize(int size) {
        this.size = size;
    }
 
    public int getNumberOfElements() {
        return numberOfElements;
    }
 
    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }
 
    public List<T> getContent() {
        return content;
    }
 
    public void setContent(List<T> content) {
        this.content = content;
    }
 
    public boolean isHasContent() {
        return isHasContent;
    }
 
    public void setHasContent(boolean isHasContent) {
        this.isHasContent = isHasContent;
    }
 
    public boolean isFirst() {
        return first;
    }
 
    public void setFirst(boolean first) {
        this.first = first;
    }
 
    public boolean isLast() {
        return last;
    }
 
    public void setLast(boolean last) {
        this.last = last;
    }
 
    public boolean isHasNext() {
        return isHasNext;
    }
 
    public void setHasNext(boolean isHasNext) {
        this.isHasNext = isHasNext;
    }
 
    public boolean isHasPrevious() {
        return isHasPrevious;
    }
 
    public void setHasPrevious(boolean isHasPrevious) {
        this.isHasPrevious = isHasPrevious;
    }
 
    public int[] getNavigatepageNums() {
        return navigatepageNums;
    }
 
    public void setNavigatepageNums(int[] navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int[] getPageLimitNums() {
        return pageLimitNums;
    }

    public void setPageLimitNums(int[] pageLimitNums) {
        this.pageLimitNums = pageLimitNums;
    }
}
