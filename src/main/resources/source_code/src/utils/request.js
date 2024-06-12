/*引入axios包*/
import axios from "axios";
/*通过axios创建request对象,发送请求到后端*/
const request = axios.create({
    /*解读：超时时间5s*/
    timeout: 5000
})

/*request拦截器处理，可以对请求做统一处理，比如统一加token或者content-type等*/
request.interceptors.request.use(config => {
    config.headers["Content-type"] = "application/json;charset=utf-8";
    return config;
}, error => {
    /*如果请求出错就不再请求，直接拒绝*/
    return Promise.reject(error)
})

/*response拦截器：可以在调用接口响应后统一处理返回结果*/
request.interceptors.response.use(
    response => {
        let res = response.data
        /*解读：如果返回的是文件*/
        if (response.config.responseType === 'blob') {
            return res;
        }
        /*解读：如果是string，就转成json*/
        if (typeof res === 'String'){
            /*解读：如果res不为null，就进行转换成json对象*/
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    error => {
        console.log("err", error)
        /*如果请求出错就不再请求，直接拒绝*/
        return Promise.reject(error)
    }
)


/*导出request对象，在其他文件引入就可以使用*/
export default request