package com.lzyc.ybtappcal.bean;

import java.util.List;

/**
 * Created by lxx on 2017/5/12.
 */

public class GoodsGetCategoryBean {
    private List<CategoryBean> Category;

    public List<CategoryBean> getCategory() {
        return Category;
    }

    public void setCategory(List<CategoryBean> Category) {
        this.Category = Category;
    }

    public static class CategoryBean {
        /**
         * title : 全部分类
         * list : [{"ID":"1","Name":"补益用药","Icon":"http://new.yibaotongapp.com/","Hot":"","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_1.png"},{"ID":"2","Name":"儿科","Icon":"http://new.yibaotongapp.com/","Hot":"","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_2.png"},{"ID":"3","Name":"风湿骨科","Icon":"http://new.yibaotongapp.com//uploads/image/categoryicon/cate_icon_3.png","Hot":"1","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_3.png"},{"ID":"4","Name":"妇科","Icon":"http://new.yibaotongapp.com/","Hot":"","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_4.png"},{"ID":"5","Name":"感冒发烧","Icon":"http://new.yibaotongapp.com//uploads/image/categoryicon/cate_icon_5.png","Hot":"1","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_5.png"},{"ID":"6","Name":"呼吸科","Icon":"http://new.yibaotongapp.com/","Hot":"","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_6.png"},{"ID":"7","Name":"泌尿科","Icon":"http://new.yibaotongapp.com/","Hot":"","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_7.png"},{"ID":"8","Name":"男科","Icon":"http://new.yibaotongapp.com/","Hot":"","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_8.png"},{"ID":"9","Name":"内分泌科","Icon":"http://new.yibaotongapp.com/","Hot":"","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_9.png"},{"ID":"10","Name":"皮肤科","Icon":"http://new.yibaotongapp.com/","Hot":"","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_10.png"},{"ID":"11","Name":"清热消炎","Icon":"http://new.yibaotongapp.com/","Hot":"","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_11.png"},{"ID":"12","Name":"神经科","Icon":"http://new.yibaotongapp.com/","Hot":"","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_12.png"},{"ID":"13","Name":"外用常备","Icon":"http://new.yibaotongapp.com/","Hot":"","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_13.png"},{"ID":"14","Name":"微矿补益","Icon":"http://new.yibaotongapp.com/","Hot":"","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_14.png"},{"ID":"15","Name":"五官科","Icon":"http://new.yibaotongapp.com/","Hot":"","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_15.png"},{"ID":"16","Name":"消化科","Icon":"http://new.yibaotongapp.com/","Hot":"","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_16.png"},{"ID":"17","Name":"心脑血管","Icon":"http://new.yibaotongapp.com//uploads/image/categoryicon/cate_icon_17.png","Hot":"1","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_17.png"},{"ID":"18","Name":"血液科","Icon":"http://new.yibaotongapp.com/","Hot":"","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_18.png"},{"ID":"19","Name":"肿瘤科","Icon":"http://new.yibaotongapp.com/","Hot":"","Image":"http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_19.png"}]
         * count : 19
         */

        private String title;
        private int count;
        private List<ListBean> list;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * ID : 1
             * Name : 补益用药
             * Icon : http://new.yibaotongapp.com/
             * Hot :
             * Image : http://new.yibaotongapp.com//uploads/image/categoryimage/cate_image_1.png
             */

            private String ID;
            private String Name;
            private String Icon;
            private String Hot;
            private String Image;

            public String getID() {
                return ID;
            }

            public void setID(String ID) {
                this.ID = ID;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public String getIcon() {
                return Icon;
            }

            public void setIcon(String Icon) {
                this.Icon = Icon;
            }

            public String getHot() {
                return Hot;
            }

            public void setHot(String Hot) {
                this.Hot = Hot;
            }

            public String getImage() {
                return Image;
            }

            public void setImage(String Image) {
                this.Image = Image;
            }

            @Override
            public String toString() {
                return "ListBean{" +
                        "ID='" + ID + '\'' +
                        ", Name='" + Name + '\'' +
                        ", Icon='" + Icon + '\'' +
                        ", Hot='" + Hot + '\'' +
                        ", Image='" + Image + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "CategoryBean{" +
                    "title='" + title + '\'' +
                    ", count=" + count +
                    ", list=" + list +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GoodsGetCategoryBean{" +
                "Category=" + Category +
                '}';
    }
}
