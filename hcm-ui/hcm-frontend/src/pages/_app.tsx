import { AppProps } from 'next/app';
import { SessionProvider } from 'next-auth/react';
import '../../styles/globals.css';
import { Provider } from 'react-redux';
import { store } from '../store/store';
import { ThemeProvider, CssBaseline } from '@mui/material';
import theme from '../theme';
import ErrorBoundary from '../components/common/ErrorBoundary';
import Head from 'next/head';
import { LoaderProvider, useLoader } from '../components/common/LoaderContext';
import api from '../services/api';
import { useEffect } from 'react';
import { NextComponentType, NextPageContext } from 'next';
import { useRouter } from 'next/router';

interface AppWithLoaderProps {
  Component: NextComponentType<NextPageContext, any, any>;
  pageProps: any;
}

function AppWithLoader({ Component, pageProps }: AppWithLoaderProps) {
  const { setLoading } = useLoader();
  const router = useRouter();
  useEffect(() => {
    const reqInterceptor = api.interceptors.request.use((config) => {
      setLoading(true);
      return config;
    }, (error) => {
      setLoading(false);
      return Promise.reject(error);
    });
    const resInterceptor = api.interceptors.response.use((response) => {
      setLoading(false);
      return response;
    }, (error) => {
      setLoading(false);
      return Promise.reject(error);
    });
    const handleStart = () => setLoading(true);
    const handleComplete = () => setLoading(false);
    router.events.on('routeChangeStart', handleStart);
    router.events.on('routeChangeComplete', handleComplete);
    router.events.on('routeChangeError', handleComplete);
    return () => {
      api.interceptors.request.eject(reqInterceptor);
      api.interceptors.response.eject(resInterceptor);
      router.events.off('routeChangeStart', handleStart);
      router.events.off('routeChangeComplete', handleComplete);
      router.events.off('routeChangeError', handleComplete);
    };
  }, [setLoading, router]);
  return <Component {...pageProps} />;
}

function MyApp({ Component, pageProps }: AppProps) {
  return (
    <Provider store={store}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <SessionProvider session={pageProps.session}>
          <Head>
            <title>ezHire</title>
          </Head>
          <ErrorBoundary>
            <LoaderProvider>
              <AppWithLoader Component={Component} pageProps={pageProps} />
            </LoaderProvider>
          </ErrorBoundary>
        </SessionProvider>
      </ThemeProvider>
    </Provider>
  );
}

export default MyApp; 