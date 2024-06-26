<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >

      <a-row :gutter="24">
        <a-col :span="8">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="add()">
                  新增
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-table
              v-if="level1.length>0"
              :columns="columns"
              :row-key="record => record.id"
              :data-source="level1"
              :loading="Loading"
              :pagination="false"
              size="small"
              :default-expand-all-rows="true"
              bordered
          >
            <template #name="{ text,record}">
              {{ record.sort }} {{ text }}
            </template>

            <template v-slot:action="{ text, record }">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  编辑
                </a-button>
                <a-popconfirm
                    title="删除后不可恢复，确认删除?"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="handleDelete(record.id)"
                >
                  <a-button type="danger" size="small">
                    删除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" layout="vertical">
            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="名称"/>
            </a-form-item>
            <a-form-item>
              <a-tree-select
                  v-model:value="doc.parent"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="treeSelectData"
                  placeholder="请选择父文档"
                  tree-default-expand-all
                  :replaceFields="{label: 'name', key: 'id', value: 'id'}"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="顺序"/>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="handlePreviewContent()">
                <EyeOutlined/>
                内容预览
              </a-button>
            </a-form-item>
            <a-form-item>
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>

      <a-drawer width="900"
                placement="right"
                :closable="false"
                :visible="drawerVisible"
                @close="onDrawerClose">
        <div class="wangeditor" :innerHTML="previewHtml"></div>
      </a-drawer>
    </a-layout-content>
  </a-layout>
  <!--<a-modal-->
  <!--    title="文档表单"-->
  <!--    v-model:visible="modalVisible"-->
  <!--    :confirm-loading="modalLoading"-->
  <!--    @ok="handleModalOk">-->
  <!--  -->
  <!--</a-modal>-->
</template>


<script lang="ts">
import {createVNode, defineComponent, onMounted, ref,} from 'vue';
import axios from 'axios';
import {message, Modal} from "ant-design-vue";
import {Tool} from "@/util/tool";
import ExclamationCircleOutlined from "@ant-design/icons-vue/ExclamationCircleOutlined";
import {useRoute} from "vue-router";
import E from 'wangeditor';
import i18next from "i18next";


export default defineComponent({
      name: 'AdminDoc',
      setup() {
        //获取路由
        const route = useRoute();
        console.log(route)
        const treeSelectData = ref();
        treeSelectData.value = [];

        const param = ref();
        param.value = {};
        const docs = ref();
        const loading = ref(false);

        const doc = ref();
        doc.value = {
          ebookId: route.query.ebookId
        };

        const columns = [
          {
            title: '名称',
            dataIndex: 'name',
            slots: {customRender: 'name'}
          },
          {
            title: 'Action',
            key: 'action',
            slots: {customRender: 'action'}
          }

        ];

        /*一级分类树
        [{
          id:"",
          name:"",
          children:[{
            id:"",
            name:"",
          }]
        }]
         */
        const level1 = ref(); //一级分类树，chiLdren属性就是二级分类
        level1.value = []
        //数据查询
        const handleQuery = () => {
          loading.value = true;
          level1.value = [];
          axios.get("/doc/all/" + route.query.ebookId).then((response) => {
            loading.value = false;
            const data = response.data;
            if (data.success) {
              docs.value = data.content;
              console.log("原始数组:", docs.value);
              level1.value = [];
              //递归
              level1.value = Tool.array2Tree(docs.value, 0);
              console.log("树形结构：", level1);

              // 父文档下拉框初始化，相当于点击新增
              treeSelectData.value = Tool.copy(level1.value) || [];
              // 为选择树添加一个"无"
              treeSelectData.value.unshift({id: 0, name: '无'});

            } else {
              message.error(data.message)
            }
          });
        };


        //--------表单----------
        const modalVisible = ref(false);
        const modalLoading = ref(false);

        const editor = new E('#content');
        editor.i18next = i18next;

        editor.config.zIndex = 0

        //保存
        const handleSave = () => {
          modalLoading.value = true;
          doc.value.content = editor.txt.html();
          axios.post("/doc/save", doc.value).then((response) => {
            modalLoading.value = false;
            const data = response.data;
            if (data.success) {
              // modalVisible.value = false;
              message.success("保存成功！")
              //刷新当前列表
              handleQuery();
            } else {
              message.error(data.message)
            }
          });
        };

        /**
         * 将某节点及其子孙节点全部置为disabled
         */
        const setDisable = (treeSelectData: any, id: any) => {
          // console.log(treeSelectData, id);
          // 遍历数组，即遍历某一层节点
          for (let i = 0; i < treeSelectData.length; i++) {
            const node = treeSelectData[i];
            if (node.id === id) {
              // 如果当前节点就是目标节点
              console.log("disabled", node);
              // 将目标节点设置为disabled
              node.disabled = true;

              // 遍历所有子节点，将所有子节点全部都加上disabled
              const children = node.children;
              if (Tool.isNotEmpty(children)) {
                for (let j = 0; j < children.length; j++) {
                  setDisable(children, children[j].id)
                }
              }
            } else {
              // 如果当前节点不是目标节点，则到其子节点再找找看。
              const children = node.children;
              if (Tool.isNotEmpty(children)) {
                setDisable(children, id);
              }
            }
          }
        };

        //删除二次确认
        //todo 二次确认弹窗无法取消
        const handleDelete = (id: number) => {
          // console.log(level1, level1.value, id)
          // 清空数组，否则多次删除时，数组会一直增加
          deleteIds.length = 0;
          deleteNames.length = 0;
          getDeleteIds(level1.value, id);
          Modal.confirm({
            title: '重要提醒',
            icon: createVNode(ExclamationCircleOutlined),
            content: '将删除：【' + deleteNames.join(",") + "】删除后不可恢复，确认删除？",
            onOk() {
              console.log(id)
              return axios.delete("/doc/delete/" + deleteIds.join(",")).then((response) => {
                const data = response.data; // data = commonResp
                if (data.success) {
                  // 重新加载列表
                  handleQuery();
                } else {
                  message.error(data.message);
                }
              }).catch((error)=>{
                console.error('Delete Failed',error);
                message.error('删除失败')
              });
            },
          });
        };


        const deleteIds: Array<string> = [];
        const deleteNames: Array<string> = [];
        const getDeleteIds = (treeSelectData: any, id: any) => {
          // console.log(treeSelectData, id);
          // 遍历数组，即遍历某一层节点
          for (let i = 0; i < treeSelectData.length; i++) {
            const node = treeSelectData[i];
            if (node.id === id) {
              // 如果当前节点就是目标节点
              console.log("delete", node);
              // 将目标节点设置为disabled
              // node.disabled = true;
              deleteIds.push(id);
              deleteNames.push(node.name);

              // 遍历所有子节点，将所有子节点全部都加上disabled
              const children = node.children;
              if (Tool.isNotEmpty(children)) {
                for (let j = 0; j < children.length; j++) {
                  getDeleteIds(children, children[j].id)
                }
              }
            } else {
              // 如果当前节点不是目标节点，则到其子节点再找找看。
              const children = node.children;
              if (Tool.isNotEmpty(children)) {
                getDeleteIds(children, id);
              }
            }
          }
        };

        /**
         * 内容查询
         */
        const handleQueryContent = () => {
          axios.get("/doc/find-content/" + doc.value.id).then((response) => {
            loading.value = false;
            const data = response.data;
            if (data.success) {
              editor.txt.html(data.content)
            } else {
              message.error(data.message)
            }
          });
        };

        //编辑
        const edit = (record: any) => {
          editor.txt.html("");
          modalVisible.value = true;
          //实现编辑时复制对象，修改表单不影响数据
          doc.value = Tool.copy(record);
          handleQueryContent();
          // 不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
          treeSelectData.value = Tool.copy(level1.value);
          setDisable(treeSelectData.value, record.id);

          // 为选择树添加一个"无"
          treeSelectData.value.unshift({id: 0, name: '无'});
        };

        //新增
        const add = () => {
          editor.txt.html("");
          modalVisible.value = true;
          doc.value = {
            ebookId: route.query.ebookId
          }

          treeSelectData.value = Tool.copy(level1.value);
          // 为选择树添加一个"无"
          treeSelectData.value.unshift({id: 0, name: '无'});
        };

        //删除
        const deleteBook = (id: number) => {
          getDeleteIds(level1.value, id);
          axios.delete("/doc/delete/" + deleteIds.join(",")).then((response) => {
            const data = response.data;
            if (data.success) {
              //刷新当前列表
              handleQuery();
            } else {
              message.error(data.message)
            }
          });
        };

        // ----------------富文本预览--------------
        const drawerVisible = ref(false);
        const previewHtml = ref();
        const handlePreviewContent = () => {
          const html = editor.txt.html();
          previewHtml.value = html;
          drawerVisible.value = true;
        };
        const onDrawerClose = () => {
          drawerVisible.value = false;
        };


        onMounted(() => {
          handleQuery();
          editor.create();
        });

        return {
          param,
          level1,
          columns,
          loading,
          deleteBook,
          handleQuery,

          handleDelete,

          edit,
          add,

          doc,
          modalVisible,
          modalLoading,
          handleSave,

          treeSelectData,

          drawerVisible,
          previewHtml,
          handlePreviewContent,
          onDrawerClose,
        }
      }
    }
)
;


</script>

<style scoped>
.editable-row-operations a {
  margin-right: 8px;
}
</style>