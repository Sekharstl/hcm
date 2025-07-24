import NextAuth from "next-auth";
import OktaProvider from "next-auth/providers/okta";

export default NextAuth({
  providers: [
    OktaProvider({
      clientId: process.env.OKTA_CLIENT_ID!,
      clientSecret: process.env.OKTA_CLIENT_SECRET!,
      issuer: process.env.OKTA_ISSUER!,
    }),
  ],
  session: {
    strategy: "jwt",
  },
  callbacks: {
    async jwt({ token, account, profile }) {
      if (account && account.id_token) {
        console.log('Raw ID Token:', account.id_token);
      }
      if (profile) {
        console.log('Okta profile:', profile);
      }
      // Add groups from the Okta profile to the JWT token
      if (profile && (profile as any).groups) {
        token.groups = (profile as any).groups;
      }
      return token;
    },
    async session({ session, token }) {
      // Expose groups to the session
      if (token.groups) {
        (session.user as any).groups = token.groups;
      }
      return session;
    },
  },
}); 