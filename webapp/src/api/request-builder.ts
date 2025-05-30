import type {Schema} from "zod";
import {ApiClient, type ApiRequestBody, type HttpMethod} from "~/api/api-client";

export class RequestBuilder<
    R,
    Q extends Record<string, unknown> | undefined = undefined,
    B extends ApiRequestBody = undefined,
> {
    private method?: HttpMethod;
    private url?: string;
    private querySchema?: Schema<Q>;
    private bodySchema?: Schema<B>;
    private responseSchema?: Schema<R>;

    constructor(private baseUrl: string) {
    }

    get(path: string) {
        this.method = 'GET';
        this.url = `${this.baseUrl}${path}`;
        return this;
    }

    post(path: string) {
        this.method = 'POST';
        this.url = `${this.baseUrl}${path}`;
        return this;
    }

    put(path: string) {
        this.method = 'PUT';
        this.url = `${this.baseUrl}${path}`;
        return this;
    }

    patch(path: string) {
        this.method = 'PATCH';
        this.url = `${this.baseUrl}${path}`;
        return this;
    }

    delete(path: string) {
        this.method = 'DELETE';
        this.url = `${this.baseUrl}${path}`;
        return this;
    }

    withQuery<NQ extends Record<string, unknown>>(schema: Schema<NQ>): RequestBuilder<R, NQ, B> {
        const builder = new RequestBuilder<R, NQ, B>(this.baseUrl);
        Object.assign(builder, this);
        builder.querySchema = schema;
        return builder;
    }

    withBody<NB extends ApiRequestBody>(schema: Schema<NB>): RequestBuilder<R, Q, NB> {
        const builder = new RequestBuilder<R, Q, NB>(this.baseUrl);
        Object.assign(builder, this);
        builder.bodySchema = schema;
        return builder;
    }

    withResponse<NR>(schema: Schema<NR>): RequestBuilder<NR, Q, B> {
        const builder = new RequestBuilder<NR, Q, B>(this.baseUrl);
        Object.assign(builder, this);
        builder.responseSchema = schema;
        return builder;
    }

    async execute(params?: { body?: B, query?: Q }): Promise<R> {
        if (!this.method || !this.url) {
            throw new Error('Method and url must be set before executing the request');
        }
        const apiClient = new ApiClient();

        const queryParsed = this.executeParse<Q>(this.querySchema, params?.query);
        const bodyParsed = this.executeParse<B>(this.bodySchema, params?.body);

        const response = await apiClient.request<R, B>({
            method: this.method,
            url: this.url,
            body: bodyParsed,
            query: queryParsed,
        })

        if (!response.success) {
            throw new Error(response.errorCode);
        }
        return this.executeParse(this.responseSchema, response.data);
    }

    private executeParse<D>(schema?: Schema, data?: D): D {
        try {
            return schema ? schema.parse(data) : data as D;
        } catch (e) {
            console.debug(e);
            throw new Error('Error in parsing data');
        }
    }
}
