class Wrapper {
    static withHandler(func) {
        return (that) => {
            that.setState({
                success: false,
                error: null,
            });
            return this.withErrorHandler(() => func.call(that)
                .then(() => {
                    that.setState({
                        success: true,
                        error: null,
                    });
                }))(that);
        };
    }

    static withErrorHandler(func) {
        return (that) => {
            return func.call(that)
                .catch((error) => {
                    console.error(error.response);
                    that.setState({
                        success: false,
                        error: error.response.data.message || error.response.data.error || error.response.data,
                    });
                });
        };
    }

    static dismiss(that) {
        return () => that.setState({
                success: false,
                error: null,
            }
        );
    }
}

export default Wrapper;