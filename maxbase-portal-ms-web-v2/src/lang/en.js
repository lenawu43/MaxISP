let data = {
  header: {
    title: ''
  },
  login: {
    title: 'Clound Platform',
    savePwd: 'Remeber Me',
    login: 'Log In'
  }
};
module.exports = {
  data,
  addData(key, data) {
    if (data[key]) {
      data[key] = data;
    } else {
      // console.warn('已存在key值为' + key + '的数据');
    }
  }
}