import { GetServerSidePropsContext } from "next";
import { getSession, signIn } from "next-auth/react";

export async function getServerSideProps(context: GetServerSidePropsContext) {
  const session = await getSession(context);
  if (session) {
    return {
      redirect: {
        destination: "/dashboard",
        permanent: false,
      },
    };
  }
  return { props: {} };
}

export default function Home() {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen">
      <h1 className="text-2xl font-bold mb-4">Welcome! Please log in.</h1>
      <button
        className="btn-primary"
        onClick={() => signIn("okta", { callbackUrl: "/dashboard" })}
      >
        Sign in with Okta
      </button>
    </div>
  );
} 