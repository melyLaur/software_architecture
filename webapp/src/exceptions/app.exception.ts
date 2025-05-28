export class AppException extends Error {
    constructor(message = 'Unknown error') {
        super(message);
        this.name = "AppException";
    }
}