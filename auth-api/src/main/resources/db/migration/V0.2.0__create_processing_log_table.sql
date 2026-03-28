CREATE TABLE public.processing_log
(
    id uuid,
    user_id uuid NOT NULL,
    input_text text NOT NULL,
    output_text text NOT NULL,
    created_at timestamp NOT NULL,

    CONSTRAINT pk_processing_log PRIMARY KEY (id),
    CONSTRAINT fk_users_processing_log FOREIGN KEY (user_id) REFERENCES public.users (id)
);