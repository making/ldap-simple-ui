import axios from 'axios';

class UserService {
    loadMe() {
        this.source = axios.CancelToken.source();
        return axios.get(`/me`, {
            cancelToken: this.source.token
        });
    }

    updateMe(user) {
        return axios.put(`/me`, user);
    }

    loadUser(userId) {
        this.source = axios.CancelToken.source();
        return axios.get(`/users/${userId}`, {
            cancelToken: this.source.token
        });
    }

    createUser(user) {
        return axios.post('/users', user,);
    }

    updateUser(user) {
        return axios.put(`/users/${user.id}`, user);
    }

    deleteUser(userId) {
        return axios.delete(`/users/${userId}`);
    }

    cancel() {
        if (this.source) {
            this.source.cancel('Operation canceled by the user.');
        }
    }
}

export default new UserService();