import{l as p}from"./index.e3edb5c8.js";import{_ as b,r,o as D,c as y,w as t,a as o,d}from"./index.215449f9.js";const v={data(){return{formData:{url:"",name:"",isPublic:!0}}},methods:{onSubmit(m){var e=this;e.formData.isPublic=m,e.formData.uname=e.$cookies.get("name"),e.$request.post("/downloadProxy",p.stringify(e.formData)).then(n=>{e.$message.success(n.data.msg)}).catch(n=>{e.$message.error(n)})}}},x=d("Download To Public"),V=d("Download To Private");function w(m,e,n,P,l,s){const i=r("el-input"),u=r("el-form-item"),_=r("el-button"),f=r("el-form"),c=r("el-container");return D(),y(c,{class:"form-container"},{default:t(()=>[o(f,{model:l.formData,class:"demo-form-inline","label-position":"top"},{default:t(()=>[o(u,{label:"Url"},{default:t(()=>[o(i,{modelValue:l.formData.url,"onUpdate:modelValue":e[0]||(e[0]=a=>l.formData.url=a),placeholder:"Url / M3U8 Url",clearable:""},null,8,["modelValue"])]),_:1}),o(u,{label:"File Name"},{default:t(()=>[o(i,{modelValue:l.formData.name,"onUpdate:modelValue":e[1]||(e[1]=a=>l.formData.name=a),placeholder:"File Name",clearable:""},null,8,["modelValue"])]),_:1}),o(_,{type:"primary",onClick:e[2]||(e[2]=a=>s.onSubmit(!0))},{default:t(()=>[x]),_:1}),o(_,{type:"primary",onClick:e[3]||(e[3]=a=>s.onSubmit(!1))},{default:t(()=>[V]),_:1})]),_:1},8,["model"])]),_:1})}var k=b(v,[["render",w]]);export{k as default};
