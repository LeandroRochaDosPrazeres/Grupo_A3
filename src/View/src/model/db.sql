-- WARNING: This schema is for context only and is not meant to be run.
-- Table order and constraints may not be valid for execution.

CREATE TABLE public.assets (
  id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
  ticker character varying NOT NULL UNIQUE,
  name character varying NOT NULL,
  category character varying NOT NULL,
  base_risk numeric,
  CONSTRAINT assets_pkey PRIMARY KEY (id)
);
CREATE TABLE public.investors (
  id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
  name character varying NOT NULL,
  document_id character varying NOT NULL UNIQUE,
  risk_profile character varying NOT NULL CHECK (risk_profile::text = ANY (ARRAY['CONSERVATIVE'::character varying, 'MODERATE'::character varying, 'AGGRESSIVE'::character varying]::text[])),
  responsible_manager_id bigint,
  created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT investors_pkey PRIMARY KEY (id),
  CONSTRAINT investors_responsible_manager_id_fkey FOREIGN KEY (responsible_manager_id) REFERENCES public.users(id)
);
CREATE TABLE public.logs (
  id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
  user_id bigint,
  action character varying NOT NULL,
  details text,
  created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT logs_pkey PRIMARY KEY (id),
  CONSTRAINT logs_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id)
);
CREATE TABLE public.optimizations (
  id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
  portfolio_id bigint NOT NULL,
  run_by_user_id bigint,
  expected_return numeric,
  total_risk numeric,
  created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT optimizations_pkey PRIMARY KEY (id),
  CONSTRAINT optimizations_portfolio_id_fkey FOREIGN KEY (portfolio_id) REFERENCES public.portfolios(id),
  CONSTRAINT optimizations_run_by_user_id_fkey FOREIGN KEY (run_by_user_id) REFERENCES public.users(id)
);
CREATE TABLE public.portfolio_items (
  id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
  portfolio_id bigint NOT NULL,
  asset_id bigint NOT NULL,
  quantity numeric NOT NULL,
  average_price numeric NOT NULL,
  suggested_percentage numeric,
  CONSTRAINT portfolio_items_pkey PRIMARY KEY (id),
  CONSTRAINT portfolio_items_portfolio_id_fkey FOREIGN KEY (portfolio_id) REFERENCES public.portfolios(id),
  CONSTRAINT portfolio_items_asset_id_fkey FOREIGN KEY (asset_id) REFERENCES public.assets(id)
);
CREATE TABLE public.portfolio_prices (
  date text NOT NULL,
  ticker character varying NOT NULL,
  price text NOT NULL,
  CONSTRAINT portfolio_prices_pkey PRIMARY KEY (date, ticker),
  CONSTRAINT portfolio_prices_ticker_fkey FOREIGN KEY (ticker) REFERENCES public.assets(ticker)
);
CREATE TABLE public.portfolios (
  id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
  investor_id bigint NOT NULL,
  name character varying NOT NULL,
  desired_risk_level numeric,
  created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT portfolios_pkey PRIMARY KEY (id),
  CONSTRAINT portfolios_investor_id_fkey FOREIGN KEY (investor_id) REFERENCES public.investors(id)
);
CREATE TABLE public.users (
  id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
  name character varying NOT NULL,
  email character varying NOT NULL UNIQUE,
  password_hash character varying NOT NULL,
  role character varying NOT NULL CHECK (role::text = ANY (ARRAY['ADMIN'::character varying, 'MANAGER'::character varying, 'INVESTOR'::character varying]::text[])),
  manager_code character varying,
  active boolean NOT NULL DEFAULT true,
  created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT users_pkey PRIMARY KEY (id)
);
